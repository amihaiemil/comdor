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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import co.comdor.Action;

import com.google.common.collect.Lists;
import com.jcabi.github.Comment;
import com.jcabi.github.Coordinates;
import com.jcabi.github.Github;
import com.jcabi.github.Issue;
import com.jcabi.github.Repos;
import com.jcabi.github.mock.MkGithub;
import com.jcabi.github.mock.MkStorage;

/**
 * Unit tests for {@link Chat}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class GithubActionTestCase {

    /**
     * More Actions are executed concurrently.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void actionsExecuteConcurrently() throws Exception {
        try {
            final Language english = (Language)new English();
            final Issue issue1 = this.githubIssue("amihaiemil", "@comdor hello there");
            final Issue issue2 = this.githubIssue("jeff", "@comdor hello");
            final Issue issue3 = this.githubIssue("vlad", "@comdor, hello");
            final Action ac1 = new Chat(issue1);
            final Action ac2 = new Chat(issue2);
            final Action ac3 = new Chat(issue3);
            
            final ExecutorService executorService = Executors.newFixedThreadPool(5);
            final List<Future> futures = new ArrayList<>();
            futures.add(executorService.submit(() -> {
                try {
                    ac1.perform();
                } catch (IOException ex) {
                    throw new IllegalStateException();
                }
            }));
            futures.add(executorService.submit(() -> {
                try {
                    ac2.perform();
                } catch (IOException ex) {
                    throw new IllegalStateException();
                }
            }));
            futures.add(executorService.submit(() -> {
                try {
                    ac3.perform();
                } catch (IOException ex) {
                    throw new IllegalStateException();
                }
            }));
            
            for(final Future f : futures) {
                MatcherAssert.assertThat(f.get(), Matchers.nullValue());
            }
            
            final List<Comment> commentsWithReply1 = Lists.newArrayList(issue1.comments().iterate());
            final List<Comment> commentsWithReply2 = Lists.newArrayList(issue2.comments().iterate());
            final List<Comment> commentsWithReply3 = Lists.newArrayList(issue3.comments().iterate());
            
            final String expectedReply1 = String.format(english.response("hello.comment"),"amihaiemil");
            MatcherAssert.assertThat(
                    commentsWithReply1.get(1).json().getString("body"),
                    Matchers.equalTo(expectedReply1)
            );
            
            final String expectedReply2 = String.format(english.response("hello.comment"),"jeff");
            MatcherAssert.assertThat(
                    commentsWithReply2.get(1).json().getString("body"),
                    Matchers.equalTo(expectedReply2)
            );
            
            final String expectedReply3 = String.format(english.response("hello.comment"),"vlad");
            MatcherAssert.assertThat(
                    commentsWithReply3.get(1).json().getString("body"),
                    Matchers.equalTo(expectedReply3)
            );
        } catch (IOException ex) {
            Logger.getLogger(GithubActionTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates an Issue with the given command.
     * @param author Author of the comment;
     * @param comment The comment's body;
     * @return Github issue
     * @throws Exception If something goes wrong.
     */
    private Issue githubIssue(
        final String author, final String comment
    ) throws Exception {
        final MkStorage storage = new MkStorage.InFile();
        final Github commanderGh = new MkGithub(storage, author);
        commanderGh.repos().create(new Repos.RepoCreate("testrepo", false));
        final Coordinates repoCoordinates = new Coordinates.Simple(
            author, "testrepo"
        );
        final Issue issue = commanderGh.repos().get(repoCoordinates).issues()
            .create("Test issue for commands", "test body");
        issue.comments().post(comment);
        return new MkGithub(storage, "comdor")
            .repos().get(repoCoordinates).issues().get(issue.number());

    }
}
