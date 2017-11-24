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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * Language that the bot speaks.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public abstract class Language {
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(
        Language.class.getName()
    );

    /**
     * Commands that the agent can understand, in a given language.
     */
    private Properties commands = new Properties();

    /**
     * Responses that the agent can give, in a given language.
     */
    private Properties responses = new Properties();

    /**
     * Ctor.
     * @param commandsFileName Properties file with commands.
     * @param responsesFileName Properties file with responses.
     */
    public Language(final String commandsFileName, final String responsesFileName) {
        try {
            this.commands.load(
                this.getClass().getClassLoader()
                    .getResourceAsStream(commandsFileName)
            );
            this.responses.load(
                this.getClass().getClassLoader()
                    .getResourceAsStream(responsesFileName)
            );
        } catch (final IOException ex) {
            LOG.error("Exception when loading commands' patterns!", ex);
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Categorize the given mention.
     * @param mention Mention where the bot has been called.
     * @return String type.
     * @throws IOException If something goes wrong with the call to Github.
     * @checkstyle
     */
    public String categorize(final Command mention) throws IOException {
        final Set<Object> keys = this.commands.keySet();
        String type = "unknown";
        for(final Object key : keys) {
            boolean match = true;
            final String keyString = (String) key;
            final String[] words = this.commands.getProperty(keyString, "")
                .split("\\^");
            for(final String word : words) {
                if(!mention.json().getString("body").contains(word.trim())) {
                    match = false;
                }
            }
            if(match) {
                type = keyString.split("\\.")[0];
                break;
            }
        }
        return type;
    }

    /**
     * Find a response.
     * @param key The response's key.
     * @return String.
     */
    public String response(final String key) {
        return this.responses.getProperty(key);
    }
}
