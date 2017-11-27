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
public final class FireUpDocker extends IntermediaryStep {

    /**
     * Docker host where containers will run.
     */
    private DockerHost host;
    
    /**
     * Ctor. Docker host will run based on the environment variables.
     * @param next The next step to perform.
     */
    public FireUpDocker(final Step next) {
        this(
            "", "", next
        );
    }
    
    /**
     * Ctor. Docker host will run remotely at the specified coordinates.
     * @param next The next step to perform.
     * @param dockerHost Https-scheme ip + port of the remote docker host.
     * @param dockerCertPath Path, on disk, to the certificates.
     */
    public FireUpDocker(
        final String dockerHost, final String dockerCertPath, final Step next
    ) {
        this(new RtDockerHost(dockerHost, dockerCertPath), next);
    }

    /**
     * Ctor. Docker host will run remotely at the specified coordinates.
     * @param next The next step to perform.
     * @param host Docker host where the containers will run.
     */
    public FireUpDocker(final DockerHost host, final Step next) {
        super(next);
        this.host = host;
    }
    
    @Override
    public void perform(
        final Command command, final Log log
    ) throws IOException {
        try(
            final Container container = this.host
                .connect()
                .create(command.comdorYaml().docker()).start()
        ) {
            container.execute(command.scripts(), log.logger());
        }
        this.next().perform(command, log);
    }
}
