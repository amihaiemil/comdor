/**
 * Copyright (c) 2017, Mihai Emil Andronache
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1)Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 2)Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 3)Neither the name of comdor nor the names of its
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
package co.comdor.github;

import co.comdor.Action;
import co.comdor.Log;
import co.comdor.LogFile;
import co.comdor.SocialSteps;
import co.comdor.Steps;
import co.comdor.WebLog;
import com.jcabi.github.Issue;
import java.io.IOException;
import java.util.UUID;

/**
 * Action that the agent takes, on Github.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class GithubAction implements Action {

    /**
     * This action's id.
     */
    private String id;
    
    /**
     * Github Issue which triggered this action.
     */
    private Issue issue;
    
    /**
     * Social steps to be executed in the end, after the bot finishes everything
     * else.
     */
    private SocialSteps social;
    
    /**
     * Log of this action. Each Github action should be logged in its own file,
     * since we want to let the user inspect the logs sometimes.
     */
    private Log log;
    
    /**
     * Ctor.
     * @param issue Github Issue which triggered this action.
     * @param social Social steps that the bot executes after the he fulfills
     *  the triggering Mention.
     * @throws IOException If there is any IO problem (e.g. writing files,
     *  communicating with Github etc).
     */
    public GithubAction(
        final Issue issue, final SocialSteps social
    ) throws IOException {
        this.issue = issue;
        this.social = social;
        this.id = UUID.randomUUID().toString();
        this.log = new WebLog(
            new LogFile(
                System.getProperty("LOG_ROOT") + "/comdor/ActionLogs", this.id
            ),
            System.getProperty("comdor.logs.endpoint")
        );
    }
    
    /**
     * Builds and executes all the steps. Catches exceptions and logs them.
     * If everything fails with IOException, it tries to post a final error
     * reply, pointing the user to the action's logs.
     */
    @Override
    public void perform() {
        try {
            this.log.logger().info("Started action " + this.id);
            final Conversation talk = new Conversation(
                new Hello(
                    new RunScript(
                        new Confused()
                    )
                )
            );
            final Mention mention = new CachedMention(
                new LastMention(this.issue)
            );
            final Steps steps = talk.start(mention);
            steps.perform(this.log);
            this.social.perform(mention, this.log);
        } catch (final MentionLookupException mle) {
            this.log.logger().warn(mle.getMessage());
        } catch (final IOException ioe) {
            this.log.logger().error(
                "Action failed entirely with exception: ",  ioe
            );
            try {
                this.issue.comments().post(
                    String.format(
                        "There was an error when processing your command. "
                        + "[Here](%s) are the logs.",
                        this.log.location()
                    )
                );
            } catch (final IOException errEx) {
                this.log.logger().error("FAILED TO SEND ERROR REPLY!", errEx);
            }
        }
    }
    
}
