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

import co.comdor.Log;
import co.comdor.PreconditionCheckStep;
import co.comdor.Step;

import java.io.IOException;

/**
 * Step where it is checked that at least one architect is
 * defined in .comdor.yml.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final  class ArchitectsRequired extends PreconditionCheckStep{

    /**
     * Ctor.
     * @param onTrue Step to perform if there is at least one architect.
     * @param onFalse Step to perform if there is not architect defined.
     */
    public ArchitectsRequired(final Step onTrue, final Step onFalse) {
        super(onTrue, onFalse);
    }

    @Override
    public void perform(
        final Command command, final Log log) throws IOException {
        final String author = command.author();
        log.logger().info(
            "At least an architect is needed for this..."
        );
        if(command.comdorYaml().architects().isEmpty()) {
            log.logger().warn("No architects defined!");
            this.onFalse().perform(command, log);
        } else {
            log.logger().info("Found at least an architect - OK!");
            this.onTrue().perform(command, log);
        }
    }
}
