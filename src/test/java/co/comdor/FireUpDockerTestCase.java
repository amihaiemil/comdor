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

import co.comdor.github.ComdorYaml;
import co.comdor.github.Command;
import com.amihaiemil.docker.Container;
import com.amihaiemil.docker.Containers;
import com.amihaiemil.docker.Docker;
import com.amihaiemil.docker.Logs;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.hamcrest.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;

import javax.json.Json;
import java.io.IOException;
import java.io.StringReader;

/**
 * Unit tests for {@link FireUpDocker}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.3
 */
public final class FireUpDockerTestCase {

    /**
     * FireUpDocker performs OK.
     * The container is started, scripts are executed
     * and the container is closed.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void performsOk() throws Exception {
        final Logs logs = Mockito.mock(Logs.class);
        Mockito.when(logs.follow()).thenReturn(
            new StringReader("container logs")
        );
        final Container container = Mockito.mock(Container.class);
        Mockito.when(container.logs()).thenReturn(logs);
        Mockito.when(container.inspect()).thenReturn(
            Json.createObjectBuilder()
                .add(
                    "State",
                    Json.createObjectBuilder()
                        .add("ExitCode", 0)
                ).build()
        );

        final Containers containers = Mockito.mock(Containers.class);
        Mockito.when(
            containers.create(Mockito.anyString())
        ).thenReturn(container);
        final Docker host = Mockito.mock(Docker.class);
        Mockito.when(host.containers()).thenReturn(containers);

        final Step fireup = new FireUpDocker(
            host, new Step.Fake(true), new Step.Fake(false)
        );

        final Command command = Mockito.mock(Command.class);
        Mockito.when(command.comdorYaml()).thenReturn(
            new ComdorYaml.Missing()
        );
        Mockito.when(command.scripts()).thenReturn(()->{return "echo 'test'";});
        final Log log = Mockito.mock(Log.class);
        final Logger logger = Mockito.mock(Logger.class);
        Mockito.when(log.logger()).thenReturn(logger);

        fireup.perform(command, log);

        Mockito.verify(container, Mockito.times(1))
            .start();
        Mockito.verify(container, Mockito.times(1))
            .kill();
    }

    /**
     * An exception is thrown when the container is started.
     * Even if this happens, the container should still be closed.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void startThrowsException() throws Exception {
        final Container container = Mockito.mock(Container.class);
        Mockito.doThrow(new IllegalStateException("Expected")).when(
                container).start();
        final Containers containers = Mockito.mock(Containers.class);
        Mockito.when(
            containers.create(Mockito.anyString())
        ).thenReturn(container);
        final Docker host = Mockito.mock(Docker.class);
        Mockito.when(host.containers()).thenReturn(containers);

        final Step fireup = new FireUpDocker(
            host, new Step.Fake(false), new Step.Fake(false)
        );

        final Command command = Mockito.mock(Command.class);
        Mockito.when(command.comdorYaml()).thenReturn(
            new ComdorYaml.Missing()
        );
        Mockito.when(command.scripts()).thenReturn(()->{return "echo 'test'";});
        final Log log = Mockito.mock(Log.class);
        final Logger logger = Mockito.mock(Logger.class);
        Mockito.when(log.logger()).thenReturn(logger);

        try {
            fireup.perform(command, log);
        } catch (final IllegalStateException ex) {
            MatcherAssert.assertThat(
                ex.getMessage(), Matchers.equalTo("Expected")
            );
        }
        Mockito.verify(container, Mockito.times(1))
                .start();
        Mockito.verify(container, Mockito.times(1))
                .kill();
    }

}
