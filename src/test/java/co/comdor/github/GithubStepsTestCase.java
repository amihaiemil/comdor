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
        final Command comment = Mockito.mock(Command.class);
        Mockito.when(comment.json()).thenReturn(
            Json.createObjectBuilder().add("body", "@comdor run").build()
        );
        Mockito.when(comment.author()).thenReturn("amihaiemil");
        final Step toExecute = Mockito.mock(Step.class);
        
        final Steps steps = new GithubSteps(toExecute, comment);
        
        final Log log = Mockito.mock(Log.class);
        final Logger slf4j = Mockito.mock(Logger.class);
        Mockito.when(log.logger()).thenReturn(slf4j);
        
        steps.perform(log);
        
        Mockito.verify(slf4j).info("Received command: @comdor run");
        Mockito.verify(slf4j).info("Author login: amihaiemil");
        Mockito.verify(toExecute).perform(comment, log);
    }
    
    /**
     * GithubSteps executes the Steps but they trow IOException.
     * The failure message has to be sent then.
     * @throws Exception If something goes wrong.
     */
    @Test(expected = IllegalStateException.class)
    public void stepsThrowIOException() throws Exception {
        final Command comment = Mockito.mock(Command.class);
        Mockito.when(comment.language()).thenReturn(new English());
        Mockito.when(comment.author()).thenReturn("amihaiemil");
        Mockito.when(comment.json()).thenReturn(
            Json.createObjectBuilder().add("body", "@comdor run").build()
        );
        final Log log = Mockito.mock(Log.class);
        Mockito.when(log.logger()).thenReturn(Mockito.mock(Logger.class));
        Mockito.when(log.location()).thenReturn("/path/to/123.log");
        final Step toExecute = Mockito.mock(Step.class);
        Mockito.doThrow(
            new IOException("Intented IOException")
        ).when(toExecute).perform(comment, log);
        final Steps steps = new GithubSteps(toExecute, comment);
        
        steps.perform(log);
        Mockito.verify(comment).reply(
            String.format(
                new English().response("steps.failure.comment"),
                "amihaiemil",
                log.location()
            )
        );
    }
    
}
