/**
 * Copyright (c) 2017, Mihai Emil Andronache
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1)Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  2)Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *  3)Neither the name of comdor nor the names of its
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

import com.jcabi.github.Issue;

import javax.json.JsonObject;
import java.io.IOException;

/**
 * A Github Issue comment where the bot has been mentioned.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public interface Mention {

    /**
     * The mentioning comment's author.
     * @return String.
     */
    String author();

    /**
     * What type is it? 'hello', 'run' etc
     * @return String.
     */
    String type();

    /**
     * Language of this mention.
     * @return Language. Defaults to English.
     */
    Language language();

    /**
     * What scripts to run does it contain?
     * @return String.
     */
    String scripts();

    /**
     * Issue where the mention is found.
     * @return Github Issue.
     */
    Issue issue();

    /**
     * Reply to this mention.
     * @param message Message of the reply.
     * @throws IOException If the comment cannot be sent to Github.
     */
    void reply(final String message)throws IOException;

    /**
     * Tries to understand this Mention, based on the languages that it speaks.
     * @param langs Languages that the bot can speak.
     * @throws IOException If something goes wrong with the call to Github.
     */
    void understand(final Language... langs) throws IOException;

    /**
     * The entire Mention in Json, as it is returned by the
     * Github API.
     * @return JsonObject
     * @see https://developer.github.com/v3/issues/comments/
     */
    JsonObject json();
}
