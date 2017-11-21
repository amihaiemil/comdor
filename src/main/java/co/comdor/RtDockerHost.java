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

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificates;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import java.nio.file.Paths;

/**
 * The Docker host contacted via its REST api (using Spotify's docker-client).
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.3
 * @todo #58:30min Add a Decorator which catches all the checked exceptions,
 *  logs them and throws a RuntimeException further.
 */
public final class RtDockerHost implements DockerHost{

    /**
     * Docker client.
     */
    private final DockerClient client;

    /**
     * Ctor. This will create the DockerClient based on DOCKER_HOST
     * and DOCKER_CERT_PATH env vars. If these are not set, then it will
     * connect to the local dockerd.
     * @throws DockerCertificateException If the docker client cannot be built.
     */
    public RtDockerHost() throws DockerCertificateException {
        this(DefaultDockerClient.fromEnv().build());
    }

    /**
     * Ctor. This will create the DockerClient based on the given configs.
     * It should be used when connecting to a remote host.
     * @param dockerHost Https-scheme ip + port of the remote docker host.
     * @param dockerCertPath Path, on disk, to the certificates.
     * @throws DockerCertificateException If the docker client cannot be built.
     */
    public RtDockerHost(
        final String dockerHost, final String dockerCertPath
    ) throws DockerCertificateException {
        this(
           DefaultDockerClient
               .builder()
               .uri(dockerHost)
               .dockerCertificates(
                   new DockerCertificates(Paths.get(dockerCertPath))
               ).build()
        );
    }

    /**
     * Ctor.
     * @param client Docker client talking to this host.
     */
    public RtDockerHost(final DockerClient client) {
        this.client = client;
    }

    @Override
    public Container create(final String image, final String name) {
        try {
            final ContainerCreation container = this.client.createContainer(
                ContainerConfig
                    .builder()
                    .image(image)
                    .build(),
                name
            );
            return new Docker(container.id(), this);
        } catch (final DockerException dex) {
            throw new IllegalStateException(
                "DockerException when creating the container "
                + "with image: " + image + " & name: " + name, dex
            );
        } catch (final InterruptedException iex) {
            throw new IllegalStateException(
                "InterruptedException when creating the container "
                + "with image: " + image + " & name: " + name, iex
            );
        }
    }

    @Override
    public void start(final String containterId) {
        try {
            this.client.startContainer(containterId);
        } catch (final DockerException dex) {
            throw new IllegalStateException(
                    "DockerException when starting container "
                            + "with id " + containterId, dex
            );
        } catch (final InterruptedException iex) {
            throw new IllegalStateException(
                    "InterruptedException when starting container "
                            + "with id " + containterId, iex
            );
        }
    }

    @Override
    public void remove(final String containerId) {
        try {
            this.client.removeContainer(containerId);
        } catch (final DockerException dex) {
            throw new IllegalStateException(
                "DockerException when removing container "
                + "with id " + containerId, dex
            );
        } catch (final InterruptedException iex) {
            throw new IllegalStateException(
                "InterruptedException when removing container "
                + "with id " + containerId, iex
            );
        }
    }
}