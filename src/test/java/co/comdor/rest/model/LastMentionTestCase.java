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

import co.comdor.github.LastMention;
import co.comdor.github.Mention;
import com.jcabi.github.*;
import com.jcabi.github.mock.MkGithub;
import com.jcabi.github.mock.MkStorage;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import javax.json.JsonObject;

/**
 * Unit tests for {@link co.comdor.github.LastMention}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class LastMentionTestCase {

    /**
     * LastMention doesn't know its type. This functionality has
     * to be implemented in a decorator.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void typeIsUnknown() throws Exception {
        final MkStorage storage = new MkStorage.Synced(new MkStorage.InFile());
        final Repo repoMihai = new MkGithub(storage, "amihaiemil")
                .repos().create(
                        new Repos.RepoCreate("amihaiemil.github.io", false)
                );
        final Issue mihai = repoMihai.issues().create("test issue", "body");
        mihai.comments().post("@comdor hello!");//first mention

        final Issue comdor = new MkGithub(storage, "comdor").repos()
                .get(repoMihai.coordinates())
                .issues()
                .get(mihai.number());
        MatcherAssert.assertThat(
                new LastMention(comdor).type(),
                Matchers.equalTo("unknown")
        );
    }

    /**
     * The agent is not mentioned in the comments at all.
     * @throws Exception if something goes wrong.
     */
    @Test
    public void noMentionAtAll() throws Exception {
        final MkStorage storage = new MkStorage.Synced(new MkStorage.InFile());
        final Repo repoMihai = new MkGithub(storage, "amihaiemil")
                .repos().create(
                        new Repos.RepoCreate("amihaiemil.github.io", false)
                );
        final Issue mihai = repoMihai.issues()
                .create("test issue", "body");
        mihai.comments().post("@george hello!");
        mihai.comments().post("@someoneelse, please check that...");

        final Issue comdor = new MkGithub(storage, "comdor").repos()
                .get(repoMihai.coordinates()).issues().get(mihai.number());

        try {
            new LastMention(comdor);
            Assert.fail("An IAE should have been thrown!");
        } catch (final IllegalArgumentException iae) {
            MatcherAssert.assertThat(
                    iae.getMessage(),
                    Matchers.equalTo("No mention found!")
            );
        }
    }

    /**
     * Agent already replied once to the last comment.
     * @throws Exception if something goes wrong.
     */
    @Test
    public void mentionAlreadyReplied() throws Exception {
        final MkStorage storage = new MkStorage.Synced(new MkStorage.InFile());
        final Repo repoMihai = new MkGithub(storage, "amihaiemil")
            .repos().create(
                new Repos.RepoCreate("amihaiemil.github.io", false)
            );
        final Issue mihai = repoMihai.issues()
            .create("test issue", "body");
        mihai.comments().post("@comdor hello!");

        final Issue comdor = new MkGithub(storage, "comdor").repos()
            .get(repoMihai.coordinates()).issues().get(mihai.number());
        comdor.comments().post("@amihaiemil hi there");

        mihai.comments().post("@someoneelse, please check that...");

        try {
            new LastMention(comdor);
            Assert.fail("An IAE should have been thrown!");
        } catch (final IllegalArgumentException iae) {
            MatcherAssert.assertThat(
                iae.getMessage(),
                Matchers.equalTo("Last mention is already answered!")
            );
        }

    }

    /**
     * There is more than 1 mention of the agent in the issue and it has already
     * replied to others, but the last one is not replied to yet.
     * @throws Exception if something goes wrong.
     */
    @Test
    public void previousMentionReplied() throws Exception {
        final MkStorage storage = new MkStorage.Synced(new MkStorage.InFile());
        final Repo repoMihai = new MkGithub(storage, "amihaiemil")
            .repos().create(
                new Repos.RepoCreate("amihaiemil.github.io", false)
            );
        final Issue mihai = repoMihai.issues().create("test issue", "body");
        mihai.comments().post("@comdor hello!");//first mention

        final Issue comdor = new MkGithub(storage, "comdor").repos()
            .get(repoMihai.coordinates())
            .issues()
            .get(mihai.number());
        comdor.comments().post("@amihaiemil hi there... ");

        mihai.comments().post("@comdor hello again!!");
        mihai.comments().post("@someoneelse, please check that...");

        MatcherAssert.assertThat(
            new LastMention(comdor).json().getString("body"),
            Matchers.equalTo("@comdor hello again!!")
        );
    }

    /**
     * There are multiple unreplied mentions, the agent should only
     * read the last one, to avoid spamming.
     * @throws Exception if something goes wrong.
     */
    @Test
    public void multipleUnrepliedMentions() throws Exception {
        final MkStorage storage = new MkStorage.Synced(new MkStorage.InFile());
        final Repo repoMihai = new MkGithub(storage, "amihaiemil")
                .repos().create(
                        new Repos.RepoCreate("amihaiemil.github.io", false)
                );
        final Issue mihai = repoMihai.issues().create("test issue", "body");
        mihai.comments().post("@comdor hello!");
        mihai.comments().post("@comdor hello again!");
        mihai.comments().post("@comdor hello, just this one is valid!");

        final Issue comdor = new MkGithub(storage, "comdor").repos()
                .get(repoMihai.coordinates())
                .issues()
                .get(mihai.number());
        MatcherAssert.assertThat(
                new LastMention(comdor).json().getString("body"),
                Matchers.equalTo("@comdor hello, just this one is valid!")
        );
    }

    /**
     * There is a single, unreplied, mention.
     * @throws Exception if something goes wrong.
     */
    @Test
    public void singeUnrepliedMention() throws Exception {
        final MkStorage storage = new MkStorage.Synced(new MkStorage.InFile());
        final Repo repoMihai = new MkGithub(storage, "amihaiemil")
                .repos().create(
                        new Repos.RepoCreate("amihaiemil.github.io", false)
                );
        final Issue mihai = repoMihai.issues().create("test issue", "body");
        mihai.comments().post("@comdor hello!");//first mention

        final Issue comdor = new MkGithub(storage, "comdor").repos()
                .get(repoMihai.coordinates())
                .issues()
                .get(mihai.number());
        MatcherAssert.assertThat(
            new LastMention(comdor).json().getString("body"),
            Matchers.equalTo("@comdor hello!")
        );
    }
}
