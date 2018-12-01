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

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link ComdorYamlRules}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.2
 */
public final class ComdorYamlRulesTestCase {
    
    /**
     * If there is no docker image specified in the read .comdor.yml, then
     * the default one should be returned (amihaiemil/comdor).
     */
    @Test
    public void providesDefaultDocker() {
        final ComdorYaml read = Mockito.mock(ComdorYaml.class);
        Mockito.when(read.docker()).thenReturn("");
        MatcherAssert.assertThat(
            new ComdorYamlRules(read).docker(),
            Matchers.equalTo("amihaiemil/comdor")
        );
    }
    
    /**
     * If there is a docker image specified in the read .comdor.yml, it returns
     * that instead of the default one.
     */
    @Test
    public void returnsOriginalDocker() {
        final ComdorYaml read = Mockito.mock(ComdorYaml.class);
        Mockito.when(read.docker()).thenReturn("test/image");
        MatcherAssert.assertThat(
            new ComdorYamlRules(read).docker(),
            Matchers.equalTo("test/image")
        );
    }
    
    /**
     * Doesn't touch the architects' list. It returns it "as is".
     */
    @Test
    public void returnsReadArchitects() {
        final ComdorYaml read = Mockito.mock(ComdorYaml.class);
        Mockito.when(read.architects())
            .thenReturn(new ArrayList<>())
            .thenReturn(Arrays.asList(new String[]{"amihaiemil"}))
            .thenReturn(Arrays.asList(new String[]{"amihaiemil", "joe"}));
        
        MatcherAssert.assertThat(
            new ComdorYamlRules(read).architects(),
            Matchers.iterableWithSize(0)
        );
        
        final List<String> arch1 = new ComdorYamlRules(read).architects();
        MatcherAssert.assertThat(arch1, Matchers.iterableWithSize(1));
        MatcherAssert.assertThat(arch1, Matchers.contains("amihaiemil"));

        final List<String> arch2 = new ComdorYamlRules(read).architects();
        MatcherAssert.assertThat(arch2, Matchers.iterableWithSize(2));
        MatcherAssert.assertThat(arch2, Matchers.contains("amihaiemil", "joe"));
    }

    @Test
    public void returnsTaggedArchitects() {
        final ComdorYaml read = Mockito.mock(ComdorYaml.class);
        Mockito.when(read.taggedArchitects())
                .thenReturn("")
                .thenReturn("@amihaiemil")
                .thenReturn("@amihaiemil @joe or @jana");

        MatcherAssert.assertThat(
            new ComdorYamlRules(read).taggedArchitects(),
            Matchers.equalTo("")
        );

        final String tagged1 = new ComdorYamlRules(read).taggedArchitects();
        MatcherAssert.assertThat(
            tagged1, Matchers.equalTo("@amihaiemil")
        );

        final String tagged2 = new ComdorYamlRules(read).taggedArchitects();
        MatcherAssert.assertThat(
            tagged2, Matchers.equalTo("@amihaiemil @joe or @jana")
        );
    }
    
    /**
     * Doesn't touch the commanders' list. It returns it "as is".
     */
    @Test
    public void returnsReadCommanders() {
        final ComdorYaml read = Mockito.mock(ComdorYaml.class);
        Mockito.when(read.commanders())
            .thenReturn(new ArrayList<>())
            .thenReturn(Arrays.asList(new String[]{"amihaiemil"}))
            .thenReturn(Arrays.asList(new String[]{"amihaiemil", "joe"}));
        
        MatcherAssert.assertThat(
            new ComdorYamlRules(read).commanders(),
            Matchers.iterableWithSize(0)
        );
        
        final List<String> cmd1 = new ComdorYamlRules(read).commanders();
        MatcherAssert.assertThat(cmd1, Matchers.iterableWithSize(1));
        MatcherAssert.assertThat(cmd1, Matchers.contains("amihaiemil"));

        final List<String> cmd2 = new ComdorYamlRules(read).commanders();
        MatcherAssert.assertThat(cmd2, Matchers.iterableWithSize(2));
        MatcherAssert.assertThat(cmd2, Matchers.contains("amihaiemil", "joe"));
    }

    /**
     * Doesn't touch the labels' list. It returns it "as is".
     */
    @Test
    public void returnsReadLabels() {
        final ComdorYaml read = Mockito.mock(ComdorYaml.class);
        Mockito.when(read.labels())
            .thenReturn(new ArrayList<>())
            .thenReturn(Arrays.asList(new String[]{"0.0.1"}))
            .thenReturn(Arrays.asList(new String[]{"0.0.1", "0.0.2"}));

        MatcherAssert.assertThat(
            new ComdorYamlRules(read).labels(),
            Matchers.iterableWithSize(0)
        );

        final List<String> arch1 = new ComdorYamlRules(read).labels();
        MatcherAssert.assertThat(arch1, Matchers.iterableWithSize(1));
        MatcherAssert.assertThat(arch1, Matchers.contains("0.0.1"));

        final List<String> arch2 = new ComdorYamlRules(read).labels();
        MatcherAssert.assertThat(arch2, Matchers.iterableWithSize(2));
        MatcherAssert.assertThat(arch2, Matchers.contains("0.0.1", "0.0.2"));
    }
}
