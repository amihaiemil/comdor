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

import org.slf4j.Logger;

/**
 * A Docker container where the scripts are run.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.3
 * @todo #58:30min Continue implementation of execute and close methods.
 */
public final class Docker implements Container {

    /**
     * This container's id.
     */
    private final String id;

    /**
     * The docker host where this container is.
     */
    private final DockerHost docker;

    /**
     * Is this container started or not?
     */
    private final boolean started;

    /**
     * Ctor.
     * @param id This container's id.
     * @param docker The docker host where it is running.
     */
    public Docker(final String id, final DockerHost docker) {
        this(id, docker, false);
    }

    /**
     * Ctor.
     * @param id This container's id.
     * @param docker The docker client.
     * @param started Is this container started or not?
     */
    private Docker(
        final String id, final DockerHost docker, final boolean started
    ) {
        this.id = id;
        this.docker = docker;
        this.started = started;
    }
    @Override
    public Container start() {
        this.docker.start(this.id);
        return new Docker(this.id, this.docker, true);
    }

    @Override
    public void execute(final String scripts, final Logger logger) {
        if(!this.started) {
            throw new IllegalStateException("Container is not started.");
        }
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean isStarted() {
        return this.started;
    }

    @Override
    public String containerId() {
        return this.id;
    }
}
