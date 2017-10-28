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
package co.comdor.rest.model;

import co.comdor.github.JsonMention;
import co.comdor.github.Mention;
import com.jcabi.github.Issue;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Unit tests for {@link co.comdor.github.JsonMention}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class JsonMentionTestCase {

    /**
     * JsonMention knows its issue, the one where it comes from.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void knowsItsIssue() throws Exception {
        final Issue parent = Mockito.mock(Issue.class);
        final Mention mention = new MockConcrete(
            Json.createObjectBuilder().build(), parent
        );
        MatcherAssert.assertThat(mention.issue() == parent, Matchers.is(true));
    }

    /**
     * JsonMention can tell us who wrote it.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void knowsItsAuthor() throws Exception {
        final JsonObject json = Json.createObjectBuilder()
            .add("user", Json.createObjectBuilder().add("login","jeff"))
            .build();
        final Mention mention = new MockConcrete(
            json, Mockito.mock(Issue.class)
        );
        MatcherAssert.assertThat(mention.author(), Matchers.equalTo("jeff"));
    }

    /**
     * JsonMention returns its Json representation.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void returnsJson() throws Exception {
        final JsonObject json = Json.createObjectBuilder()
            .add("author", "amihaiemil")
            .add("body", "@comdor hello")
            .build();
        final Mention mention = new MockConcrete(
            json, Mockito.mock(Issue.class)
        );
        MatcherAssert.assertThat(mention.json() == json, Matchers.is(true));
        MatcherAssert.assertThat(
            mention.json().getString("author"), Matchers.equalTo("amihaiemil")
        );
        MatcherAssert.assertThat(
            mention.json().getString("body"),
            Matchers.equalTo("@comdor hello")
        );
    }

    /**
     * JsonMention finds and returns the scripts from the comment's body.
     * TODO: Adapt this unit test once JsonMention.scripts() is properly
     *  implemented.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void returnsTheScripts() throws Exception {
        final Mention mention = new MockConcrete(
            Mockito.mock(JsonObject.class), Mockito.mock(Issue.class)
        );
        MatcherAssert.assertThat(
            mention.scripts(), Matchers.equalTo("echo 'hello world'")
        );
    }

    /**
     * Mock concrete class, for testing. It's better to test the final
     * methods of JsonMention separately from LastMention or other concrete
     * types, tests are easier to understand.
     */
    private static final class MockConcrete extends JsonMention {
        /**
         * Ctor.
         * @param json  Github Comment in Json.
         * @param issue Github Issue.
         */
        public MockConcrete(JsonObject json, Issue issue) {
            super(json, issue);
        }

        @Override
        public String type() {
            throw new UnsupportedOperationException("A mock, no type here.");
        }

    }
}