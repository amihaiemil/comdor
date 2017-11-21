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
package co.comdor;

import com.jcabi.github.Coordinates;
import com.jcabi.github.Github;
import com.jcabi.github.Issue;
import com.jcabi.github.Issues;
import com.jcabi.github.Repos;
import com.jcabi.github.mock.MkGithub;
import java.io.IOException;
import java.util.HashMap;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

/**
 * Unit tests for {@link VigilantAction}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.3
 */
public final class VigilantActionTestCase {
    
    /**
     * The original Action works fine, throws no exception, so no Github Issue
     * should be opened.
     * @throws Exception If something goes wrong. 
     */
    @Test
    public void originalWorksFine() throws Exception {
        final Github github = this.mockGithub();
        final Action original = this.mockAction();
        Mockito.doNothing().when(original).perform();
        
        final Action vigilant = new VigilantAction(original, github);
        vigilant.perform();
        
        final Issues all = github.repos()
            .get(new Coordinates.Simple("amihaiemil/comdor"))
            .issues();
        MatcherAssert.assertThat(
            all.iterate(new HashMap<>()),
            Matchers.iterableWithSize(0)
        );
    }
    
    /**
     * VigilantAction can open a Github issue if the original Action throws
     * an IOException.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void opensIssueOnIOException() throws Exception {
        final Github github = this.mockGithub();
        final Action original = this.mockAction();
        Mockito.doThrow(new IOException("expected")).when(original).perform();
        
        final Action vigilant = new VigilantAction(original, github);
        vigilant.perform();
        
        final Issues all = github.repos()
            .get(new Coordinates.Simple("amihaiemil/comdor"))
            .issues();
        MatcherAssert.assertThat(
            all.iterate(new HashMap<>()),
            Matchers.iterableWithSize(1)
        );
        
        final Issue opened = all.get(1);
        MatcherAssert.assertThat(
            opened.json().getString("title"),
            Matchers.equalTo("Exception occured while peforming an Action!")
        );
        
        MatcherAssert.assertThat(
            opened.json().getString("body"),
            Matchers.startsWith(
                String.format(
                    "@amihaiemil Something went wrong, please have a look."
                    + "\n\n[Here](%s) are the logs of the Action.",
                    original.log().location()
                )
                + "\n\nHere is the exception:\n\n```\n\n"
            )
        );
    }
    
    /**
     * VigilantAction can open a Github issue if the original Action throws
     * a RuntimeException.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void opensIssueOnRuntimeException() throws Exception {
        final Github github = this.mockGithub();
        final Action original = this.mockAction();
        Mockito.doThrow(new IllegalStateException("expected"))
            .when(original).perform();
        
        final Action vigilant = new VigilantAction(original, github);
        vigilant.perform();
        
        final Issues all = github.repos()
            .get(new Coordinates.Simple("amihaiemil/comdor"))
            .issues();
        MatcherAssert.assertThat(
            all.iterate(new HashMap<>()),
            Matchers.iterableWithSize(1)
        );
        
        final Issue opened = all.get(1);
        MatcherAssert.assertThat(
            opened.json().getString("title"),
            Matchers.equalTo("Exception occured while peforming an Action!")
        );
        
        MatcherAssert.assertThat(
            opened.json().getString("body"),
            Matchers.startsWith(
                String.format(
                    "@amihaiemil Something went wrong, please have a look."
                    + "\n\n[Here](%s) are the logs of the Action.",
                    original.log().location()
                )
                + "\n\nHere is the exception:\n\n```\n\n"
            )
        );
    }
    
    /**
     * Mock an Action for testing.
     * @return Action mock.
     */
    public Action mockAction() {
        final Log log = Mockito.mock(Log.class);
        Mockito.when(log.logger()).thenReturn(Mockito.mock(Logger.class));
        Mockito.when(log.location()).thenReturn("path/to/log.txt");
        final Action original = Mockito.mock(Action.class);
        Mockito.when(original.log()).thenReturn(log);
        return original;
    }
    
    /**
     * Mock Github for testing.
     * @return Action mock.
     * @throws IOException If something goes wrong.
     */
    public Github mockGithub() throws IOException {
        final Github gh = new MkGithub("amihaiemil");
        gh.repos().create(new Repos.RepoCreate("comdor", false));
        return gh;
    }
    
}
