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
 * Log which is served on the Web UI.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class WebLog implements Log {

    /**
     * Log file.
     */
    private Log onServer;
    
    /**
     * HTTP endpoint to which the log's location should be appended.
     */
    private String endpoint;
    
    /**
     * Ctor.
     * @param onServer Log on server, which should be served via the endpoint.
     * @param endpoint HTTP endpoint to which the log's location
     * should be appended.
     */
    public WebLog(final Log onServer, final String endpoint) {
        this.onServer = onServer;
        this.endpoint = endpoint;
    }
    
    /**
     * Location of the log. We append the Web endpoint
     * to its location.
     * @return String The log's web location (URL pointing to it).
     */
    @Override
    public String location() {
        final String serve;
        if(this.endpoint == null) {
            serve = "/" + this.onServer.location();
        } else {
            if(this.endpoint.endsWith("/")) {
                serve = this.endpoint + this.onServer.location();
            } else {
                serve = this.endpoint + "/" + this.onServer.location();
            }
        }
        return serve;
    }

    @Override
    public Logger logger() {
        return this.onServer.logger();
    }

}
