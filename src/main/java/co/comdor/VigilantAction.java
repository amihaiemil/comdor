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

import com.jcabi.github.Coordinates;
import com.jcabi.github.Github;
import com.jcabi.github.Issue;
import java.io.IOException;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Action which catches exceptions and reports a Github Issue in comdor's
 * repository.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.3
 * @checkstyle IllegalCatch (100 lines)
 */
public final class VigilantAction implements Action {
    
    /**
     * Decorated action.
     */
    private final Action original;
    
    /**
     * Github.
     */
    private final Github github;
    
    /**
     * Ctor.
     * @param original Decorated Action.
     * @param github Github.
     */
    public VigilantAction(final Action original, final Github github) {
        this.original = original;
        this.github = github;
    }
    
    @Override
    public void perform() throws IOException {
        try {
            this.original.perform();
        } catch (final IOException | RuntimeException ex) {
            final Issue created = this.github.repos()
                .get(new Coordinates.Simple("amihaiemil/comdor"))
                .issues()
                .create(
                    "Exception occured while peforming an Action!",
                    String.format(
                        "@amihaiemil Something went wrong, please have a look."
                        + "\n\n[Here](%s) are the logs of the Action.",
                        this.original.log().location()
                    )
                    + "\n\nHere is the exception:\n\n```\n\n"
                    + ExceptionUtils.getStackTrace(ex) + "\n\n```"
                );
            this.original.log().logger().info(
                "Opened Issue https://github.com/amihaiemil/comdor/issues/"
                + created.number()
            );
        }
    }

    @Override
    public Log log() {
        return this.original.log();
    }
}
