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
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link CachedMention}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.3
 */
public final class CachedMentionTestCase {
    
    /**
     * CachedMention can cache the scripts from the original Mention.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void cachesScripts() throws Exception {
        final Mention original = Mockito.mock(Mention.class);
        Mockito.when(original.scripts()).thenReturn("echo 'hello'");
        final Mention cached = new CachedMention(original);
        MatcherAssert.assertThat(
            cached.scripts(), Matchers.equalTo("echo 'hello'")
        );
        MatcherAssert.assertThat(
            cached.scripts(), Matchers.equalTo("echo 'hello'")
        );
        MatcherAssert.assertThat(
            cached.scripts(), Matchers.equalTo("echo 'hello'")
        );
        Mockito.verify(original, Mockito.times(1)).scripts();
    }
    
    /**
     * CachedMention can cache the ComdorYaml from the original Mention.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void cachesComdorYml() throws Exception {
        final Mention original = Mockito.mock(Mention.class);
        final ComdorYaml yml = Mockito.mock(ComdorYaml.class);
        Mockito.when(original.comdorYaml()).thenReturn(yml);
        final Mention cached = new CachedMention(original);
        MatcherAssert.assertThat(
            cached.comdorYaml(), Matchers.equalTo(yml)
        );
        MatcherAssert.assertThat(
            cached.comdorYaml(), Matchers.equalTo(yml)
        );
        MatcherAssert.assertThat(
            cached.comdorYaml(), Matchers.equalTo(yml)
        );
        Mockito.verify(original, Mockito.times(1)).comdorYaml();
    }
    
    
    /**
     * CachedMention doesn't cache the author.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void doesntCacheAuthor() throws Exception {
        final Mention original = Mockito.mock(Mention.class);
        Mockito.when(original.author()).thenReturn("john");
        final Mention cached = new CachedMention(original);
        MatcherAssert.assertThat(cached.author(), Matchers.equalTo("john"));
        MatcherAssert.assertThat(cached.author(), Matchers.equalTo("john"));
        Mockito.verify(original, Mockito.times(2)).author();
    }
    
    
    /**
     * CachedMention doesn't cache the type.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void doesntCacheType() throws Exception {
        final Mention original = Mockito.mock(Mention.class);
        Mockito.when(original.type()).thenReturn("hello");
        final Mention cached = new CachedMention(original);
        MatcherAssert.assertThat(cached.type(), Matchers.equalTo("hello"));
        MatcherAssert.assertThat(cached.type(), Matchers.equalTo("hello"));
        Mockito.verify(original, Mockito.times(2)).type();
    }
    
    /**
     * CachedMention doesn't cache the language.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void doesntCacheLanguage() throws Exception {
        final Language lang = Mockito.mock(Language.class);
        final Mention original = Mockito.mock(Mention.class);
        Mockito.when(original.language()).thenReturn(lang);
        final Mention cached = new CachedMention(original);
        MatcherAssert.assertThat(cached.language(), Matchers.equalTo(lang));
        MatcherAssert.assertThat(cached.language(), Matchers.equalTo(lang));
        Mockito.verify(original, Mockito.times(2)).language();
    }
    
    /**
     * CachedMention doesn't cache the Issue.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void doesntCacheIssue() throws Exception {
        final Issue issue = Mockito.mock(Issue.class);
        final Mention original = Mockito.mock(Mention.class);
        Mockito.when(original.issue()).thenReturn(issue);
        final Mention cached = new CachedMention(original);
        MatcherAssert.assertThat(cached.issue(), Matchers.equalTo(issue));
        MatcherAssert.assertThat(cached.issue(), Matchers.equalTo(issue));
        Mockito.verify(original, Mockito.times(2)).issue();
    }
    
    /**
     * CachedMention doesn't cache the JsonObject.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void doesntCacheJson() throws Exception {
        final JsonObject json = Mockito.mock(JsonObject.class);
        final Mention original = Mockito.mock(Mention.class);
        Mockito.when(original.json()).thenReturn(json);
        final Mention cached = new CachedMention(original);
        MatcherAssert.assertThat(cached.json(), Matchers.equalTo(json));
        MatcherAssert.assertThat(cached.json(), Matchers.equalTo(json));
        Mockito.verify(original, Mockito.times(2)).json();
    }
    
    /**
     * CachedMention replies at each call to reply(String).
     * @throws Exception If something goes wrong.
     */
    @Test
    public void repliesEverytime() throws Exception {
        final Mention original = Mockito.mock(Mention.class);
        final Mention cached = new CachedMention(original);
        cached.reply("test1");
        cached.reply("test1");
        cached.reply("test1");
        Mockito.verify(original, Mockito.times(3)).reply("test1");
    }
    
    /**
     * CachedMention understands at each call to understand(Language...).
     * @throws Exception If something goes wrong.
     */
    @Test
    public void understandsEverytime() throws Exception {
        final Mention original = Mockito.mock(Mention.class);
        final Mention cached = new CachedMention(original);
        final Language lang = Mockito.mock(Language.class);
        cached.understand(lang);
        cached.understand(lang);
        cached.understand(lang);
        Mockito.verify(original, Mockito.times(3)).understand(lang);
    }
    
}
