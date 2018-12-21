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

import org.junit.Test;
import org.mockito.Mockito;

import co.comdor.Knowledge;
import co.comdor.Log;
import co.comdor.Step;

/**
 * Unit tests for {@link Conversation}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class ConversationTestCase {

    /**
     * Conversation should try to understand the Mention before forwarding it
     * to the next Knowledge.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void understandsBeforeForwarding() throws Exception {
        final Language[] langs = {new English()};
        final Conversation conv = new Conversation(
            new Knowledge() {
                @Override
                public Step start(
                    final Command mention, final Log log
                ) throws IOException {
                    Mockito.verify(mention).understand(langs);
                    return null;
                }
            },
            langs
        );
        conv.start(Mockito.mock(Command.class), Mockito.mock(Log.class));
    }
    
    /**
     * Conversation should always forward the mention to the next Knowledge.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void followupIsCalled() throws Exception {
        final Knowledge followup = Mockito.mock(Knowledge.class);
        final Conversation conv = new Conversation(followup);
        final Command mention = Mockito.mock(Command.class);
        final Log log = Mockito.mock(Log.class);
        conv.start(mention, log);
        Mockito.verify(followup).start(mention, log);
    }
}
