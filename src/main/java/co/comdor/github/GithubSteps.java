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

import co.comdor.Step;
import co.comdor.Steps;
import co.comdor.Log;

import java.io.IOException;

/**
 * Steps to fulfill a mention, executed on Github.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class GithubSteps implements Steps {

    /**
     * Steps to be performed.
     */
    private Step steps;
    
    /**
     * Initial mention. The one that triggered everything.
     */
    private Mention mention;

    /**
     * Constructor.
     * @param steps Steps to perform everything.
     * @param mention Initial menton.
     */
    public GithubSteps(final Step steps, final Mention mention) {
        this.steps = steps;
        this.mention = mention;
    }

    /**
     * Perform all the given steps.
     * @param log Action logger.
     * @throws IOException If something goes wrong while calling Github.
     */
    @Override
    public void perform(final Log log) throws IOException {
        try {
            log.logger().info(
                "Received command: " + this.mention.json().getString("body")
            );
            log.logger().info("Author login: " + this.mention.author());
            this.steps.perform(this.mention, log);
        } catch (final IOException ex) {
            log.logger().error(
                "Some step did not execute properly, sending failure reply.", ex
            );
            this.mention.reply(
                String.format(
                    this.mention.language().response("steps.failure.comment"),
                    this.mention.author(),
                    log.location()
                )
            );
        }

    }

}
