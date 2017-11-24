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
package co.comdor.github;

import co.comdor.Log;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests {@link ArchitectsRequired}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class ArchitectsRequiredTestCase {

    /**
     * The check passes, because there are architects defined.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void architectsDefined() throws Exception {
        final ArchitectsRequired check = new ArchitectsRequired(
            (Command mention, Log log) -> {log.logger().info("OK");},
            (Command mention, Log log) -> {
                throw new IllegalStateException("architectsDefined failed!");
            }
        );

        final Command mention = Mockito.mock(Command.class);
        final ComdorYaml yaml = Mockito.mock(ComdorYaml.class);
        final List<String> architects = new ArrayList<>();
        architects.add("amihaiemil");
        Mockito.when(yaml.architects()).thenReturn(architects);
        Mockito.when(mention.comdorYaml()).thenReturn(yaml);

        final Log log = Mockito.mock(Log.class);
        Mockito.when(log.logger()).thenReturn(Mockito.mock(Logger.class));

        check.perform(mention, log);
    }

    /**
     * The check doesn't pass, because the archtiects' list is empty.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void architectsMissing() throws Exception {
        final ArchitectsRequired check = new ArchitectsRequired(
            (Command mention, Log log) -> {
                throw new IllegalStateException("architectsMissing failed!");
            },
            (Command mention, Log log) -> {log.logger().info("OK");}
        );

        final Command mention = Mockito.mock(Command.class);
        final ComdorYaml yaml = Mockito.mock(ComdorYaml.class);
        Mockito.when(yaml.architects()).thenReturn(new ArrayList<>());
        Mockito.when(mention.comdorYaml()).thenReturn(yaml);

        final Log log = Mockito.mock(Log.class);
        Mockito.when(log.logger()).thenReturn(Mockito.mock(Logger.class));

        check.perform(mention, log);
    }
}
