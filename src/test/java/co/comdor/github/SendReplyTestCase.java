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
import co.comdor.Log;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import org.slf4j.Logger;

/**
 * Unit tests for {@link SendReply}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class SendReplyTestCase {

    /**
     * {@link SendReply} sends the message and executes the next Step.
     * @throws Exception If something goes worng.
     */
    @Test
    public void replySentOk() throws Exception {
        final Mention mention = Mockito.mock(Mention.class);
        final String message = "hello";
        Mockito.doNothing().when(mention).reply(message);
        new SendReply(
            message, new Step.Fake(true)
        ).perform(mention, Mockito.mock(Log.class));
    }

    /**
     * {@link SendReply} throws IOException if the reply send fails.
     * @throws Exception If something goes worng.
     */
    @Test
    public void replyFails() throws Exception {
        final Mention mention = Mockito.mock(Mention.class);
        final String message = "hello";
        Mockito.doThrow(new IOException("This is expected, it's ok!"))
            .when(mention).reply(message);
        final Log log = Mockito.mock(Log.class);
        Mockito.when(log.logger()).thenReturn(Mockito.mock(Logger.class));
        try {
            new SendReply(
                message, new Step.Fake(false)
            ).perform(mention, log);
        } catch (final IOException ex) {
            MatcherAssert.assertThat(
                ex.getMessage(),
                Matchers.equalTo("IOException when sending a reply!")
            );
        }
    }
}
