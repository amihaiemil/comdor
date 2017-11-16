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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
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
     * Docker can start.
     */
    @Test
    public void startsContainer() {
        Container container = new Docker("amihaiemil/comdor");
        MatcherAssert.assertThat(container.isStarted(), Matchers.is(false));
        MatcherAssert.assertThat(
            container.start().isStarted(), Matchers.is(true)
        );
    }
    
    /**
     * Docker throws ISE if we try to execute something when it's not started.
     */
    @Test(expected = IllegalStateException.class)
    public void executionFailsIfContainerIsStopped() {
        Container container = new Docker("amihaiemil/comdor");
        container.execute("cloc .", Mockito.mock(Logger.class));
    }
    
    /**
     * Docker can be closed.
     * TODO: edit this test when the method will be implemented.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void containerCloses() {
        Container container = new Docker("amihaiemil/comdor");
        container.close();
    }
    
}
