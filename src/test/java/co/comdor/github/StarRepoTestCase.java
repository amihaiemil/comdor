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

import co.comdor.Step;
import co.comdor.Log;
import com.jcabi.github.Github;
import com.jcabi.github.Issue;
import com.jcabi.github.Stars;
import com.jcabi.github.Repo;
import com.jcabi.github.Repos;
import com.jcabi.github.mock.MkGithub;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

/**
 * Unit tests for {@link StarRepo}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.2
 */
public final class StarRepoTestCase {

    /**
     * StarRepo can successfully star a given repository.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void starsRepo() throws Exception {
        final Log log = Mockito.mock(Log.class);
        final Logger slf4j = Mockito.mock(Logger.class);
        Mockito.doNothing().when(slf4j).info(Mockito.anyString());
        Mockito.doThrow(
            new IllegalStateException("Unexpected error; test failed")
        ).when(slf4j).error(Mockito.anyString());
        Mockito.when(log.logger()).thenReturn(slf4j);

        final Github gh = new MkGithub("amihaiemil");
        final Repo repo =  gh.repos().create(
            new Repos.RepoCreate("amihaiemil.github.io", false)
        );
        final Mention com = Mockito.mock(Mention.class);
        final Issue issue = Mockito.mock(Issue.class);
        Mockito.when(issue.repo()).thenReturn(repo);
        Mockito.when(com.issue()).thenReturn(issue);
        
        final Step star = new StarRepo(Mockito.mock(Step.class));
        MatcherAssert.assertThat(
            com.issue().repo().stars().starred(),
            Matchers.is(false)
        );
        star.perform(com, log);
        MatcherAssert.assertThat(
            com.issue().repo().stars().starred(),
            Matchers.is(true)
        );
    }
    
    /**
     * StarRepo tries to star a repo twice.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void starsRepoTwice() throws Exception {
        final Log log = Mockito.mock(Log.class);
        final Logger slf4j = Mockito.mock(Logger.class);
        Mockito.doNothing().when(slf4j).info(Mockito.anyString());
        Mockito.doThrow(
            new IllegalStateException("Unexpected error; test failed")
        ).when(slf4j).error(Mockito.anyString());
        Mockito.when(log.logger()).thenReturn(slf4j);

        final Github gh = new MkGithub("amihaiemil");
        final Repo repo =  gh.repos().create(
            new Repos.RepoCreate("amihaiemil.github.io", false)
        );
        final Mention com = Mockito.mock(Mention.class);
        final Issue issue = Mockito.mock(Issue.class);
        Mockito.when(issue.repo()).thenReturn(repo);
        Mockito.when(com.issue()).thenReturn(issue);

        final Step star = new StarRepo(Mockito.mock(Step.class));
        MatcherAssert.assertThat(
            com.issue().repo().stars().starred(),
            Matchers.is(false)
        );
        star.perform(com, log);
        star.perform(com, log);
        MatcherAssert.assertThat(
            com.issue().repo().stars().starred(),
            Matchers.is(true)
        );
    }

    /**
     * StarRepo did not star the repository due to an IOException. 
     * StarRepo.perform() should return true anyway because it's not a critical operation and we
     * shouldn't fail the whole process just because of this.
     * 
     * This test expects an RuntimeException (we mock the logger in such a way) because it's the easiest way
     * to find out if the flow entered the catch block.
     * @throws IOException If something goes wrong.
     */
    public void repoStarringFails() throws IOException {
        final Log log = Mockito.mock(Log.class);
        final Logger slf4j = Mockito.mock(Logger.class);
        Mockito.doNothing().when(slf4j).info(Mockito.anyString());
        Mockito.when(log.logger()).thenReturn(slf4j);
        
        final Repo repo = Mockito.mock(Repo.class);
        final Stars stars = Mockito.mock(Stars.class);
        Mockito.when(stars.starred()).thenReturn(false);
        Mockito.doThrow(new IOException()).when(stars).star();
        Mockito.when(repo.stars()).thenReturn(stars);
        
        final Mention com = Mockito.mock(Mention.class);
        final Issue issue = Mockito.mock(Issue.class);
        Mockito.when(issue.repo()).thenReturn(repo);
        Mockito.when(com.issue()).thenReturn(issue);
        
        final StarRepo star = new StarRepo(Mockito.mock(Step.class));
        star.perform(com, log);
        Mockito.verify(slf4j).warn(
            "IOException when starring repository, could not star the repo."
        );
    }
}
