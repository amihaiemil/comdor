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
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ExecCreation;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * The Docker host contacted via its REST api (using Spotify's docker-client).
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.3
 */
public final class RtDockerHost implements DockerHost{

    /**
     * Docker client.
     */
    private final DockerClient client;

    /**
     * Https-scheme ip + port of the remote docker host.
     */
    private final String dockerHost;
    
    /**
     * Path, on disk, to the certificates.
     */
    private final String certPath;
    
    /**
     * Ctor. This will use the DOCKER_HOST and DOCKER_CERT_PATH env vars.
     * If these are not set, then it will connect to the local dockerd.
     */
    public RtDockerHost() {
        this(null, "", "");
    }

    /**
     * Ctor..
     * @param dockerHost Https-scheme ip + port of the remote docker host.
     * @param dockerCertPath Path, on disk, to the certificates.
     */
    public RtDockerHost(
        final String dockerHost, final String dockerCertPath
    ) {
        this(null, dockerHost, dockerCertPath);
    }

    /**
     * Ctor.
     * @param client Docker client talking to this host.
     * @param dockerHost Https-scheme ip + port of the remote docker host.
     * @param dockerCertPath Path, on disk, to the certificates.
     */
    private RtDockerHost(
        final DockerClient client,
        final String dockerHost, final String dockerCertPath
    ) {
        this.client = client;
        this.dockerHost = dockerHost;
        this.certPath = dockerCertPath;
    }

    @Override
    public DockerHost connect() throws IOException {
        try {
            final DockerHost connected;
            if(this.dockerHost.isEmpty() || this.certPath.isEmpty()) {
                connected = new RtDockerHost(
                    DefaultDockerClient.fromEnv().build(),
                    this.dockerHost, this.certPath
                );
            } else {
                connected = new RtDockerHost(
                    DefaultDockerClient
                        .builder()
                        .uri(this.dockerHost)
                        .dockerCertificates(
                            new DockerCertificates(Paths.get(this.certPath))
                        ).build(),
                    this.dockerHost, this.certPath
                );
            }
            return connected;
        } catch (final DockerCertificateException ex) {
            throw new IOException(
                "DockerCertificateException when trying to connect", ex
            );
        }
    }
    
    @Override
    public Container create(final String image, final String scripts) {
        if(this.client == null) {
            throw new IllegalStateException(
                "Not connected. Don't forget to get a connected "
                + "instance by calling #connect()"
            );
        }
        
        try {
            final ContainerCreation container = this.client.createContainer(
                ContainerConfig
                    .builder()
                    .image(image)
                    .cmd("/bin/bash", "-c", scripts)
                    .build()
            );
            return new Docker(container.id(), this);
        } catch (final DockerException | InterruptedException ex) {
            throw new IllegalStateException(
                "Exception when creating the container "
                + "with image: " + image + " and scripts: " + scripts, ex
            );
        }
    }

    @Override
    public void start(final String containerId) {
        if(this.client == null) {
            throw new IllegalStateException(
                "Not connected. Don't forget to get a connected "
                + "instance by calling #connect()"
            );
        }
        
        try {
            this.client.startContainer(containerId);
        } catch (final DockerException | InterruptedException ex) {
            throw new IllegalStateException(
                "Exception when starting container with id " + containerId, ex
            );
        }
    }

    @Override
    public void remove(final String containerId) {
        if(this.client == null) {
            throw new IllegalStateException(
                "Not connected. Don't forget to get a connected "
                + "instance by calling #connect()"
            );
        }
        try {
            this.client.removeContainer(containerId);
        } catch (final DockerException | InterruptedException ex) {
            throw new IllegalStateException(
                "Exception when removing container with id " + containerId, ex
            );
        }
    }

    @Override
    public void kill(final String containerId) {
        if(this.client == null) {
            throw new IllegalStateException(
                "Not connected. Don't forget to get a connected "
                + "instance by calling #connect()"
            );
        }
        try {
            this.client.killContainer(containerId);
        } catch (final DockerException | InterruptedException ex) {
            throw new IllegalStateException(
                "Exception when killing container with id " + containerId, ex
            );
        }
    }

}
