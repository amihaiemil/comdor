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
import org.slf4j.Logger;
import java.io.IOException;

/**
 * Step where the bot replies to a {@link Mention}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class SendReply extends IntermediaryStep {

    /**
     * Reply to send.
     */
    private String rep;

    /**
     * Ctor with just the reply.
     * SendReply is usually the last one in the chain, so this is
     * for convenience.
     * @param rep The reply to be sent.
     */
    public SendReply(final String rep) {
        this(rep, new Step.FinalStep());
    }

    /**
     * Ctor.
     * @param rep The reply to be sent.
     * @param next The next step to perform.
     */
    public SendReply(final String rep, final Step next) {
        super(next);
        this.rep = rep;
    }

    @Override
    public void perform(
        final Mention mention, final Logger log
    ) throws IOException {
        try {
            mention.reply(this.rep);
        } catch (final IOException ex) {
            log.error("IOException when sending a reply!", ex);
            throw new IOException("IOException when sending a reply!", ex);
        }
        this.next().perform(mention, log);
    }
}
