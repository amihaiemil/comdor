/**
 * Copyright (c) 2016-2017, Mihai Emil Andronache
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1)Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  2)Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *  3)Neither the name of charles-rest nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package co.comdor.rest;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.comdor.Action;
import co.comdor.SystemProperties;
import co.comdor.VigilantAction;
import co.comdor.github.Chat;
import co.comdor.rest.model.Notification;
import co.comdor.rest.model.Notifications;
import co.comdor.rest.model.SimplifiedNotifications;
import co.comdor.rest.model.WebhookNotifications;

import com.jcabi.github.Coordinates;
import com.jcabi.github.Github;
import com.jcabi.github.RtGithub;
import com.jcabi.http.wire.RetryWire;

/**
 * REST interface to receive Github notifications for chatting.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @checkstyle DesignForExtension (300 lines)
 * @checkstyle ClassDataAbstractionCoupling (300 lines)
 */
@Path("/")
@Stateless
public class ChatResource {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(
        ChatResource.class.getName()
    );

    /**
     * The http request.
     */
    @Context
    private HttpServletRequest request;

    /**
     * Consumes a JsonArray consisting of Github notifications json objects.
     * The <b>notifications are simplified</b>: a notification json looks
     * like this:
     * <pre>
     * {
     *     "repoFullName":"amihaiemil/myrepo",
     *     "issueNumber":23
     * }
     * </pre>
     * This info is enough. Since we have the repo and the issue that
     * triggered the notification, we can easily find out the earliest
     * comment where the Github agent was tagged.
     * <br><br>
     * <b>IMPORTANT:</b><br>
     * Each call to this endpoint has to contain the <b>Authorization
     * http header</b>, with a token <b>agreed upon</b> by this service
     * and the notification checker.<br>Technically, it might as well be the
     * Github auth token since it has to be the same on both parties,
     * but this is really sensitive information
     * and we should pass it around as little as possible.<br><br>
     * 
     * @param notifications Json array of simplified Github notifications.
     * @return Http Response.
     */
    @POST
    @Path("notifications")
    public Response postNotifications(final String notifications) {
        final String token = this.request.getHeader(HttpHeaders.AUTHORIZATION);
        final int status;
        if(token == null || token.isEmpty()) {
            status = HttpURLConnection.HTTP_FORBIDDEN;
        } else {
            final String key = new SystemProperties.GithubApiToken()
                .toString();
            if(token.equals(key)) {
                final boolean startedHandling = this.handleNotifications(
                    new SimplifiedNotifications(notifications)
                );
                if(startedHandling) {
                    status = HttpURLConnection.HTTP_OK;
                } else {
                    status = HttpURLConnection.HTTP_INTERNAL_ERROR;
                }
            } else {
                LOG.error(
                    "Missing or incorrect comdor.auth.token! "
                    + "Notifications post is FORBIDDEN!"
                );
                status = HttpURLConnection.HTTP_FORBIDDEN;
            }
        }
        return Response.status(status).build();
    }

    /**
     * Webhook for Github issue_comment event.
     * @param issueComment Event Json payload.
     * @see <a href="https://developer.github.com/v3/activity/events/types">
     *     Webhook Events Payloads
     * </a>
     * @return Http response.
     */
    @POST
    @Path("/github/issuecomment")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response webhook(final JsonObject issueComment) {
        final int status;
        final String event = this.request.getHeader("X-Github-Event");
        String userAgent = this.request.getHeader("User-Agent");
        if(userAgent == null) {
            userAgent = "";
        }
        if(userAgent.startsWith("GitHub-Hookshot/")) {
            if("ping".equalsIgnoreCase(event)) {
                status = HttpURLConnection.HTTP_OK;
            } else {
                if ("issue_comment".equalsIgnoreCase(event)) {
                    final boolean startedHandling = this.handleNotifications(
                        new WebhookNotifications(issueComment)
                    );
                    if(startedHandling) {
                        status = HttpURLConnection.HTTP_OK;
                    } else {
                        status = HttpURLConnection.HTTP_INTERNAL_ERROR;
                    }

                } else {
                    status = HttpURLConnection.HTTP_PRECON_FAILED;
                }
            }
        } else {
            status = HttpURLConnection.HTTP_PRECON_FAILED;
        }
        return Response.status(status).build();
    }

    /**
     * Handles notifications, starts one action thread for each of them.
     * @param notifications List of notifications.
     * @return true if actions were started successfully; false otherwise.
     */
    private boolean handleNotifications(final Notifications notifications) {
        final String auth = new SystemProperties.GithubApiToken().toString();
        boolean handled;
        if(auth == null || auth.isEmpty()) {
            LOG.error(
                "Missing comdor.auth.token; "
                + "Please specify the Github api access token!"
            );
            handled = false;
        } else {
            final Github github = new RtGithub(
                new RtGithub(auth).entry().through(RetryWire.class)
            );
            try {
                for(final Notification notification : notifications) {
                    this.take(
                        new VigilantAction(
                            new Chat(
                                github.repos().get(
                                    new Coordinates.Simple(
                                        notification.repoFullName()
                                    )
                                ).issues().get(notification.issueNumber())
                            ),
                            github
                        )
                    );
                }
                handled = true;
            } catch (final IOException ex) {
                LOG.error("IOException when getting the Github Issue", ex);
                handled = false;
            }
        }
        return handled;
    }

    /**
     * Take an action.
     * @param action Given action.
     */
    @Asynchronous
    private void take(final Action action) {
        try {
            action.perform();
        } catch (final IOException ex) {
            LOG.error(
                "IOException when performing the Action! "
                + "Probably didn't even manage to report a Github Issue!", ex
            );
        }
    }
}
