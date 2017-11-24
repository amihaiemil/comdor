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

import co.comdor.IntermediaryStep;
import co.comdor.Step;
import com.jcabi.http.Request;
import java.io.IOException;
import java.net.HttpURLConnection;
import co.comdor.Log;

/**
 * Step where the bot follows the Github user.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.2
 */
public final class FollowUser extends IntermediaryStep {

    /**
     * Ctor.
     * @param next Next step to execute.
     */
    public FollowUser(final Step next) {
        super(next);
    }
    
    @Override
    public void perform(
        final Command command, final Log log) throws IOException {
        final String author = command.author();
        final Request follow = command.issue().repo().github().entry()
            .uri().path("/user/following/").path(author).back()
            .method("PUT");
        log.logger().info("Following Github user " + author + " ...");
        try {
            final int status = follow.fetch().status();
            if(status != HttpURLConnection.HTTP_NO_CONTENT) {
                log.logger().error(
                    "User follow status response is " + status
                    + " . Should have been 204 (NO CONTENT)"
                );
            } else {
                log.logger().info("Followed user " + author + " .");
            }
        } catch (final IOException ex) {
            log.logger().warn("IOException while trying to follow the user.");
        }
        this.next().perform(command, log);
    }
    
}
