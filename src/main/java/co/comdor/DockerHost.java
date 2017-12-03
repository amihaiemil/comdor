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

import java.io.IOException;
import org.slf4j.Logger;

/**
 * The Docker host where comdor runs the containers.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.3
 */
public interface DockerHost {

    /**
     * Connect to this DockerHost.
     * @return Connected DockerHost.
     * @throws IOException If DockerCertificateException occurs when trying to
     *  connect.
     */
    DockerHost connect() throws IOException;
    
    /**
     * Create a container based on the given image.
     * @param image Docker image.
     * @param scripts Scripts to run inside the craeted container.
     * @return The created Container.
     */
    Container create(final String image, final String scripts);

    /**
     * Start a container.
     * @param containterId The container's id.
     */
    void start(final String containterId);

    /**
     * Follow a container's logs.
     * @param containerId The container's id.
     * @param logger Logger to record the output into.
     */
    void followLogs(final String containerId, final Logger logger);

    /**
     * Kill a container.
     * @param containerId The container's Id.
     */
    void kill(final String containerId);

    /**
     * Remove a container.
     * @param containerId The container's Id.
     */
    void remove(final String containerId);

}
