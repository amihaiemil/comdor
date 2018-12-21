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

import co.comdor.*;
import com.jcabi.github.Issue;
import java.io.IOException;
import java.util.UUID;

/**
 * The bot chats with the user based on a mention.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @checkstyle ClassDataAbstractionCoupling (300 lines)
 */
public final class Chat implements Action {

    /**
     * This action's id.
     */
    private String id;
    
    /**
     * Github Issue which triggered this action.
     */
    private Issue issue;
    
    /**
     * Log of this action. Each Github action should be logged in its own file,
     * since we want to let the user inspect the logs sometimes.
     */
    private Log log;
    
    /**
     * Ctor.
     * @param issue Github Issue which triggered this action.
     * @throws IOException If there is any IO problem (e.g. writing files,
     *  communicating with Github etc).
     */
    public Chat(final Issue issue) throws IOException {
        this.issue = issue;
        this.id = UUID.randomUUID().toString();
        this.log = new WebLog(
            new LogFile(
                new SystemProperties.LogRoot() + "/comdor/ActionLogs", this.id
            ),
            new SystemProperties.LogsEndpoint().toString()
        );
    }
    
    /**
     * Builds and executes all the steps. Catches exceptions and logs them.
     * If everything fails with IOException, it tries to post a final error
     * reply, pointing the user to the action's logs.
     * @throws IOException If some IO problems occur.
     */
    @Override
    public void perform() throws IOException {
        try {
            this.log.logger().info("Started action " + this.id);
            final Conversation talk = new Conversation(
                new Hello(
                    new CreateLabels(
                        new RunScript(
                            new Confused()
                        )
                    )
                )
            );
            final Command mention = new CachedMention(
                new LastMention(this.issue)
            );
            new Social(
                new Careful(
                    talk.start(mention, this.log)
                )
            ).perform(mention, this.log);
        } catch (final MentionLookupException mle) {
            this.log.logger().warn(mle.getMessage());
        }
    }

    @Override
    public Log log() {
        return this.log;
    }
    
}
