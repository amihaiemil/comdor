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
import java.io.IOException;
import javax.json.JsonObject;

/**
 * A caching decorator for a Mention. We don't cache all the values, just the
 * ones that are expensive to fetch (e.g. .comdor.yml, scripts etc).
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.3
 */
public final class CachedMention implements Mention {

    /**
     * Cached .comdor.yml.
     */
    private ComdorYaml comdorYaml;
    
    /**
     * Cached scripts.
     */
    private String scripts;
    
    /**
     * Decorated mention.
     */
    private final Mention original;

    /**
     * Ctor.
     * @param original Mention which has to have caching.
     */
    public CachedMention(final Mention original) {
        this.original = original;
    }
    
    @Override
    public String author() {
        return this.original.author();
    }

    @Override
    public String type() {
        return this.original.type();
    }

    @Override
    public Language language() {
        return this.original.language();
    }

    @Override
    public String scripts() {
        if(this.scripts == null) {
            this.scripts = this.original.scripts();
        }
        return this.scripts;
    }

    @Override
    public Issue issue() {
        return this.original.issue();
    }

    @Override
    public ComdorYaml comdorYaml() throws IOException {
        if(this.comdorYaml == null) {
            this.comdorYaml = this.original.comdorYaml();
        }
        return this.comdorYaml;
    }

    @Override
    public void reply(final String message) throws IOException {
        this.original.reply(message);
    }

    @Override
    public void understand(final Language... langs) throws IOException {
        this.original.understand(langs);
    }

    @Override
    public JsonObject json() {
        return this.original.json();
    }
    
}
