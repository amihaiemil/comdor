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

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;

/**
 * Tests for {@link LogFile}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class LogFileTestCase {

    /**
     * LogFile creates the log file on disk.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void createsFile() throws Exception {
    	final String id = UUID.randomUUID().toString();
        final Log file = new LogFile("./src/test/resources/log_tests/", id);
        MatcherAssert.assertThat(file.location(), Matchers.endsWith(".log"));
        MatcherAssert.assertThat(file.logger(), Matchers.notNullValue());
        final File logs = new File("./src/test/resources/log_tests/" + id + ".log");
        MatcherAssert.assertThat(logs.exists(), Matchers.is(Boolean.TRUE));
        MatcherAssert.assertThat(logs.isFile(), Matchers.is(Boolean.TRUE));
    }
    
    /**
     * LogFile can return the Logger.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void hasTheLogger() throws Exception {
        final Log file = new LogFile(
            "./src/test/resources/log_tests/",
            UUID.randomUUID().toString()
        );
        MatcherAssert.assertThat(file.logger(), Matchers.notNullValue());
    }
    
    /**
     * LogFile can return the location of the log file..
     * @throws Exception If something goes wrong.
     */
    @Test
    public void hasTheLocation() throws Exception {
    	final String id = UUID.randomUUID().toString();
        final Log file = new LogFile("./src/test/resources/log_tests/", id);
        MatcherAssert.assertThat(file.location(), Matchers.endsWith(id + ".log"));
    }
    
}
