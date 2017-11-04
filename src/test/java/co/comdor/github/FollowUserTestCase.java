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
import com.jcabi.github.Repo;
import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import com.jcabi.http.mock.MkQuery;
import com.jcabi.http.request.ApacheRequest;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

/**
 * Unit tests for {@link FollowUser}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.2
 */
@SuppressWarnings("resource")
public final class FollowUserTestCase {
    
    /**
     * Follow can follow the commander.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void fullowsUserSuccessfuly() throws Exception {
        int port = this.port();
        final MkContainer github = new MkGrizzlyContainer().next(
            new MkAnswer.Simple(HttpURLConnection.HTTP_NO_CONTENT)
        ).start(port);

        final Mention com = this.mockMention();
        Mockito.when(com.issue().repo().github().entry())
            .thenReturn(new ApacheRequest("http://localhost:" + port + "/"));
        
        final Log log = Mockito.mock(Log.class);
        final Logger slf4j = Mockito.mock(Logger.class);
        Mockito.when(log.logger()).thenReturn(slf4j);
        
        try {
            new FollowUser(new Step.Fake(true)).perform(com, log);
            
            Mockito.verify(slf4j).info(
                "Following Github user " + com.author() + " ..."
            );
            Mockito.verify(slf4j).info("Followed user " + com.author() + " .");
            final MkQuery request = github.take();
            MatcherAssert.assertThat(
                request.uri().toString(),
                    Matchers.equalTo("/user/following/" + com.author())
            );
            MatcherAssert.assertThat(request.method(), Matchers.equalTo("PUT"));
        } finally {
            github.stop();
        }
    }
    
    /**
     * Follow can stay silent if the response status is not the expected one.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void differentResponseStatus() throws Exception {
        int port = this.port();
        final MkContainer github = new MkGrizzlyContainer().next(
            new MkAnswer.Simple(HttpURLConnection.HTTP_INTERNAL_ERROR)
        ).start(port);
        
        final Mention com = this.mockMention();
        Mockito.when(com.issue().repo().github().entry())
            .thenReturn(new ApacheRequest("http://localhost:" + port + "/"));
        
        final Log log = Mockito.mock(Log.class);
        final Logger slf4j = Mockito.mock(Logger.class);
        Mockito.when(log.logger()).thenReturn(slf4j);

        try {
            new FollowUser(new Step.Fake(true)).perform(com, log);
            
            Mockito.verify(slf4j).info(
                "Following Github user " + com.author() + " ..."
            );
            Mockito.verify(slf4j).error(
                "User follow status response is "
                + HttpURLConnection.HTTP_INTERNAL_ERROR
                + " . Should have been 204 (NO CONTENT)"
            );
            final MkQuery request = github.take();
            MatcherAssert.assertThat(
                request.uri().toString(),
                Matchers.equalTo("/user/following/" + com.author())
            );
            MatcherAssert.assertThat(request.method(), Matchers.equalTo("PUT"));
        } finally {
            github.stop();
        }
    }
    
    /**
     * Follow can stay silent if there's an exception.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void serverIsDown() throws Exception {
        final Mention com = this.mockMention();
        Mockito.when(com.issue().repo().github().entry())
            .thenReturn(
                new ApacheRequest("http://localhost:" + this.port() + "/")
            );
        final Log log = Mockito.mock(Log.class);
        final Logger slf4j = Mockito.mock(Logger.class);
        Mockito.when(log.logger()).thenReturn(slf4j);

        new FollowUser(new Step.Fake(true)).perform(com, log);
        
        Mockito.verify(slf4j).info(
            "Following Github user " + com.author() + " ..."
        );
        Mockito.verify(slf4j).warn(
            "IOException while trying to follow the user."
        );
    }

    /**
     * Find a free port.
     * @return A free port.
     * @throws IOException If something goes wrong.
     */
    private int port() throws IOException {
        try (final ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    /**
     * Mock a Mention, add issue, repo and github mocks into it.
     * @return Mention.
     */
    private Mention mockMention() {
        final Mention com = Mockito.mock(Mention.class);
        Mockito.when(com.author()).thenReturn("amihaiemil");
        
        final Issue issue = Mockito.mock(Issue.class);
        final Repo repo = Mockito.mock(Repo.class);
        Mockito.when(repo.github()).thenReturn(Mockito.mock(Github.class));
        Mockito.when(issue.repo()).thenReturn(repo);
        
        Mockito.when(com.issue()).thenReturn(issue);
        
        return com;
    }
}
