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
package co.comdor;

import java.io.IOException;

import co.comdor.github.Command;

/**
 * Careful steps. It watches for exceptions and informs the user
 * if anything went wrong.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class Careful implements Step {

    /**
     * Steps to be performed.
     */
    private Step steps;

    /**
     * Constructor.
     * @param steps Steps to perform everything.
     */
    public Careful(final Step steps) {
        this.steps = steps;
    }

    /**
     * Perform all the given steps.
     * 
     * Exceptions are caught, a comment is sent to the user (in the spoken 
     * language) and then they are rethrown -- they should be caught by 
     * {@link VigilanteAction} which should open an Issue in comdor's Repo.
     * 
     * @param command Command.
     * @param log Action logger.
     * @throws IOException If something goes wrong while calling Github.
     * @checkstyle IllegalCatch (50 lines)
     */
    @Override
    public void perform(
        final Command command, final Log log
    ) throws IOException {
        try {
            log.logger().info(
                "Received command: " + command.json().getString("body")
            );
            log.logger().info("Author login: " + command.author());
            this.steps.perform(command, log);
        } catch (final IOException | RuntimeException ex) {
            log.logger().error(
                "Some step did not execute properly, sending failure reply.",
                ex
            );
            command.reply(
                String.format(
                    command.language().response("steps.failure.comment"),
                    command.author(),
                    log.location()
                )
            );
            throw new IllegalStateException(
                "An exception occured in Issue: "
              + command.issue().toString(),
                ex
            );
        }

    }

}
