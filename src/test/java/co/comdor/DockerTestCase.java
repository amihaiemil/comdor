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

import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.ContainerState;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

/**
 * Unit tests for {@link Docker}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.3
 */
public final class DockerTestCase {

    /**
     * Docker can be started and then closed.
     */
    @Test
    public void containerStartsAndCloses() {
        final DockerHost host = Mockito.mock(DockerHost.class);
        final Container created = new Docker(
            "id", host
        );

        MatcherAssert.assertThat(created.isStarted(), Matchers.is(false));
        created.start();
        Mockito.verify(host, Mockito.times(1)).start(created.containerId());
        MatcherAssert.assertThat(created.isStarted(), Matchers.is(true));

        created.close();
        Mockito.verify(host, Mockito.times(1)).kill(created.containerId());
        MatcherAssert.assertThat(created.isStarted(), Matchers.is(false));
    }

    /**
     * Docker throws IllegalStateException if the container is still
     * running when exitCode() is called.
     */
    @Test
    public void exitCodeComplainsIfStillRunning() {
        final Container docker = new Docker(
            "id123", Mockito.mock(DockerHost.class)
        );
        docker.start();
        try {
            docker.exitCode();
            Assert.fail("ISE should have been thrown by now!");
        } catch (final IllegalStateException ex) {
            MatcherAssert.assertThat(
                ex.getMessage(),
                Matchers.equalTo("Container id123 is still running.")
            );
        }

    }

    /**
     * Docker returns its exit code if it is stopped.
     */
    @Test
    public void exitCodeWhenStopped() {
        final ContainerState state = Mockito.mock(ContainerState.class);
        Mockito.when(state.exitCode()).thenReturn(127);

        final ContainerInfo info = Mockito.mock(ContainerInfo.class);
        Mockito.when(info.state()).thenReturn(state);

        final DockerHost host = Mockito.mock(DockerHost.class);
        Mockito.when(host.inspect("id456")).thenReturn(info);

        final Container docker = new Docker("id456", host);
        MatcherAssert.assertThat(docker.exitCode(), Matchers.is(127));
    }
    
}
