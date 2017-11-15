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
 */
public final class Docker implements Container {

    /**
     * Is this container started or not?
     */
    private final boolean started;
    
    /**
     * Image of this container.
     */
    private final String image;
    
    /**
     * Ctor.
     * @param image Image for this container.
     */
    public Docker(final String image) {
        this(false, image);
    }
    
    /**
     * Private ctor (for immutability).
     * @param started Is it started, or not?
     * @param image Image for this container.
     */
    private Docker(final boolean started, final String image) {
        this.started = started;
        this.image = image;
    }
    
    @Override
    public Container start() {
        return new Docker(true, this.image);
    }

    @Override
    public void execute(final String scripts, final Logger logger) {
        if(!this.started) {
            throw new IllegalStateException("Docker container not started!");
        }
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
