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
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log file of an action.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class LogFile implements Log {

    /**
     * Id of the log file.
     */
    private String id;

    /**
     * Logger.
     */
    private Logger logger;
    
    /**
     * Ctor.
     * @param dir The directory where the file should be located.
     * @param id Id of the log file.
     * @throws IOException If the log file cannot be created/configured.
     */
    public LogFile(final String dir, final String id) throws IOException {
        this.id = id;
        this.setup(dir);
    }
    
    @Override
    public String location() {
        return this.id + ".log";
    }

    @Override
    public Logger logger() {
        return this.logger;
    }

    /**
     * Setup the Log4J logger. We do it programatically since the properties
     * file way is not thread-safe!<br>
     * Also, note that we have to create the file ourselves since FileAppender
     * acts funny under linux if the file doesn't already exist.
     * @param dir The directory where the file should be located.
     * @throws IOException If there's something wrong with the File.
     */
    private void setup(final String dir) throws IOException {
        final String loggerName = "Action_" + this.id;
        final org.apache.log4j.Logger build = org.apache.log4j.Logger
            .getLogger(loggerName);
        
        final String logFilePath;
        if(dir == null) {
            logFilePath = "/" + this.id + ".log";
        } else {
            if(dir.endsWith("/")) {
                logFilePath = dir + this.id + ".log";
            } else {
                logFilePath = dir + "/" + this.id  + ".log";
            }
        }
        
        final File logFile = new File(logFilePath);
        logFile.getParentFile().mkdirs();
        logFile.createNewFile();

        final FileAppender appender = new FileAppender(
            new PatternLayout("%d %p - %m%n"), logFilePath
        );
        appender.setName(this.id + "_appender");
        appender.setThreshold(Level.DEBUG);
        build.addAppender(appender);
        build.setLevel(Level.DEBUG);
        
        this.logger = LoggerFactory.getLogger(loggerName);
        
    }
    
    
}
