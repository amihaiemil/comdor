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

import java.io.IOException;

import javax.json.Json;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import co.comdor.Step;
import co.comdor.Steps;

/**
 * Unit tests for {@link GithubSteps}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class GithubStepsTestCase {

    /**
     * GithubSteps logs the received comment and author and
     * executes the given steps.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void logsAndExecutesSteps() throws Exception {
        final Mention comment = Mockito.mock(Mention.class);
        Mockito.when(comment.json()).thenReturn(
            Json.createObjectBuilder().add("body", "@comdor run").build()
        );
        Mockito.when(comment.author()).thenReturn("amihaiemil");
        final Step toExecute = Mockito.mock(Step.class);
        
        final Steps steps = new GithubSteps(
            toExecute, comment, new SendReply("some failure message")
        );
        
        final Logger logger = Mockito.mock(Logger.class);
        steps.perform(logger);
        
        Mockito.verify(logger).info("Received command: @comdor run");
        Mockito.verify(logger).info("Author login: amihaiemil");
        Mockito.verify(toExecute).perform(comment, logger);
    }
    
    /**
     * GithubSteps executes the Steps but they trow IOException.
     * The failure message has to be sent then.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void stepsThrowIOException() throws Exception {
        final Mention comment = Mockito.mock(Mention.class);
        Mockito.when(comment.json()).thenReturn(
            Json.createObjectBuilder().add("body", "@comdor run").build()
        );
        final Logger logger = Mockito.mock(Logger.class);
        final Step toExecute = Mockito.mock(Step.class);
        Mockito.doThrow(
            new IOException("Intented IOException")
        ).when(toExecute).perform(comment, logger);
        final Steps steps = new GithubSteps(
            toExecute, comment, new SendReply("some failure message")
        );
        
        steps.perform(logger);
        Mockito.verify(comment).reply("some failure message");
    }
    
}