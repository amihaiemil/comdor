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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.regex.Matcher;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link English}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class EnglishTestCase {

    /**
     * A 'hello' comment is understood.
     */
    @Test
    public void categorizesHelloCommands() throws Exception {
        final Command hello1 = this.mockMention("@comdor, hello there!");
        final Command hello2 = this.mockMention("@comdor, hello?!");
        final Command hello3 = this.mockMention("@comdor hello");
        final Command hello4 = this.mockMention("@comdor hello, how are you?");
        final Command hello5 = this.mockMention("@comdor hello, who are you?");

        final Language english = new English();
        MatcherAssert.assertThat(
            english.categorize(hello1), Matchers.equalTo("hello")
        );
        MatcherAssert.assertThat(
            english.categorize(hello2), Matchers.equalTo("hello")
        );
        MatcherAssert.assertThat(
            english.categorize(hello3), Matchers.equalTo("hello")
        );
        MatcherAssert.assertThat(
            english.categorize(hello4), Matchers.equalTo("hello")
        );
        MatcherAssert.assertThat(
            english.categorize(hello5), Matchers.equalTo("hello")
        );
    }

    /**
     * A 'run' comment is understood.
     */
    @Test
    public void categorizesRunCommands() throws Exception {
        final Command run1 = this.mockMention("@comdor, run ``cloc .`` pls");
        final Command run2 = this.mockMention("@comdor, run it");
        final Command run3 = this.mockMention("@comdor pls run ``mvn clean``");
        final Command run4 = this.mockMention("@comdor run");
        final Command run5 = this.mockMention("@comdor run?");

        final Language english = new English();
        MatcherAssert.assertThat(
            english.categorize(run1), Matchers.equalTo("run")
        );
        MatcherAssert.assertThat(
            english.categorize(run2), Matchers.equalTo("run")
        );
        MatcherAssert.assertThat(
            english.categorize(run3), Matchers.equalTo("run")
        );
        MatcherAssert.assertThat(
            english.categorize(run4), Matchers.equalTo("run")
        );
        MatcherAssert.assertThat(
            english.categorize(run5), Matchers.equalTo("run")
        );
    }

    /**
     * A comment is not understood.
     */
    @Test
    public void commandIsUnknown() throws Exception {
        final Command unknown1 = this.mockMention("@comdor who are you?");
        final Command unknown2 = this.mockMention("@comdor, Something else");
        final Command unknown3 = this.mockMention("@comdor howdy");

        final Language english = new English();
        MatcherAssert.assertThat(
            english.categorize(unknown1), Matchers.equalTo("unknown")
        );
        MatcherAssert.assertThat(
            english.categorize(unknown2), Matchers.equalTo("unknown")
        );
        MatcherAssert.assertThat(
            english.categorize(unknown3), Matchers.equalTo("unknown")
        );
    }

    /**
     * Mock a Mention.
     * @param message Message for it.
     * @return The mocked Mention.
     */
    private Command mockMention(final String message) {
        final Command mock = Mockito.mock(Command.class);
        final JsonObject comment = Json
            .createObjectBuilder().add("body", message)
            .build();

        Mockito.when(mock.json()).thenReturn(comment);
        return mock;
    }
}
