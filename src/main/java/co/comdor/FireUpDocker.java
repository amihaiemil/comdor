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

import co.comdor.github.Command;
import java.io.IOException;

/**
 * Step where a Docker container is fired up, scripts are executed and then 
 * the container is shut down.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.3
 */
public final class FireUpDocker extends PreconditionCheckStep {

    /**
     * Docker host where containers will run.
     */
    private DockerHost host;
    
    /**
     * Ctor. Docker host will run based on the environment variables.
     * @param onTrue Step to perform if Docker's exit code is 0.
     * @param onFalse Step to perform if Docker's exit code is not 0.
     */
    public FireUpDocker(final Step onTrue, final Step onFalse) {
        this(
            new SystemProperties.DockerHost().toString(),
            new SystemProperties.DockerCertificates().toString(),
            onTrue,
            onFalse
        );
    }
    
    /**
     * Ctor. Docker host will run remotely at the specified coordinates.
     * @param onTrue Step to perform if Docker's exit code is 0.
     * @param onFalse Step to perform if Docker's exit code is not 0.
     * @param dockerHost Https-scheme ip + port of the remote docker host.
     * @param dockerCertPath Path, on disk, to the certificates.
     * @checkstyle ParameterNumber (5 lines)
     */
    public FireUpDocker(
        final String dockerHost, final String dockerCertPath,
        final Step onTrue, final Step onFalse
    ) {
        this(new RtDockerHost(dockerHost, dockerCertPath), onTrue, onFalse);
    }

    /**
     * Ctor. Docker host will run remotely at the specified coordinates.
     * @param onTrue Step to perform if Docker's exit code is 0.
     * @param onFalse Step to perform if Docker's exit code is not 0.
     * @param host Docker host where the containers will run.
     */
    public FireUpDocker(
        final DockerHost host, final Step onTrue, final Step onFalse
    ) {
        super(onTrue, onFalse);
        this.host = host;
    }
    
    @Override
    public void perform(
        final Command command, final Log log
    ) throws IOException {
        final String scripts = command.scripts().asText();
        log.logger().info("Connecting to the Docker host...");
        final Container container = this.host
            .connect()
            .create(command.comdorYaml().docker(), scripts);
        final String id = container.containerId();
        try {
            log.logger().info(
                "Connected; starting container with id " + id
            );
            log.logger().info("Executing scripts: " + scripts);
            container.start();
            container.fetchLogs(log.logger());
        } finally {
            log.logger().info("Killing container " + id);
            container.close();
        }
        final int exitCode = container.exitCode();
        if(exitCode != 0) {
            log.logger().error(
                    "Container " + id + " exited with code: " + exitCode
            );
            this.onFalse().perform(command, log);
        } else {
            log.logger().info(
                    "Container " + id + " exited with code 0 - OK"
            );
            this.onTrue().perform(command, log);
        }
    }
}
