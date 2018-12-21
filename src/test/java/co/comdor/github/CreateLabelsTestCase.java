/**
 * Copyright (c) 2017-2018, Mihai Emil Andronache
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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import co.comdor.Careful;
import co.comdor.Knowledge;
import co.comdor.Log;
import co.comdor.Step;
import co.comdor.github.CreateLabels.Create;

import com.jcabi.github.Issue;
import com.jcabi.github.Repos.RepoCreate;
import com.jcabi.github.mock.MkGithub;

/**
 * Unit tests for {@link CreateLabels}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.3
 */
public final class CreateLabelsTestCase {

    /**
     * CreateLabels can start a 'labels' command.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void startsLabelsCommand() throws Exception {
        final Command com = Mockito.mock(Command.class);
        Mockito.when(com.type()).thenReturn("labels");
        Mockito.when(com.comdorYaml()).thenReturn(new ComdorYaml.Missing());
        Mockito.when(com.language()).thenReturn(new English());
        final Knowledge labels = new CreateLabels(
            (Command mention, Log log) -> {
                throw new IllegalStateException(
                    "'labels' command misunderstood!"
                );
            }
        );
        final Step steps = labels.start(com, Mockito.mock(Log.class));
        MatcherAssert.assertThat(steps, Matchers.notNullValue());
        MatcherAssert.assertThat(
            steps instanceof ArchitectCheck, Matchers.is(true)
        );

    }

    /**
     * CreateLabels can start a command which is not 'labels',
     * by forwarding it to the next Knowledge.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void forwardsNotLabelsCommand() throws Exception {
        final Command com = Mockito.mock(Command.class);
        Mockito.when(com.type()).thenReturn("notlabels");
        final Knowledge hello = new CreateLabels(
            (Command mention, Log log) -> {
                MatcherAssert.assertThat(
                    mention.type(),
                    Matchers.equalTo("notlabels")
                );
                return null;
            }
        );
        hello.start(com, Mockito.mock(Log.class));
    }

    /**
     * For {@link Create}. No labels are actually specified, so none is
     * created.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void missingLabels() throws Exception {
        final Command command = Mockito.mock(Command.class);
        Mockito.when(command.comdorYaml()).thenReturn(
            new ComdorYaml.Missing()
        );
        final Log log = Mockito.mock(Log.class);
        Mockito.when(log.logger()).thenReturn(
            Mockito.mock(Logger.class)
        );
        final Issue issue = new MkGithub("comdor")
            .repos()
            .create(new RepoCreate("testrepo", false))
            .issues()
            .create("test issue", "test body");
        Mockito.when(command.issue()).thenReturn(issue);
        
        MatcherAssert.assertThat(
            issue.repo().labels().iterate(),
            Matchers.is(Matchers.emptyIterable())
        );
        
        final Create create = new Create(new Step.Fake(true));
        create.perform(command, log);
        
        MatcherAssert.assertThat(
            issue.repo().labels().iterate(),
            Matchers.is(Matchers.emptyIterable())
        );
    }
    
    /**
     * For {@link Create}. Some labels are actually specified, so they
     * should be created.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void createsLabels() throws Exception {
        final ComdorYaml yaml = Mockito.mock(ComdorYaml.class);
        Mockito.when(yaml.labels()).thenReturn(
            Arrays.asList(
                new String[]{
                    "0.0.1", "0.0.2", "@amihaiemil", "@comdor"
                }
            )
        );
        final Command command = Mockito.mock(Command.class);
        Mockito.when(command.comdorYaml()).thenReturn(yaml);
        final Log log = Mockito.mock(Log.class);
        Mockito.when(log.logger()).thenReturn(
            Mockito.mock(Logger.class)
        );
        final Issue issue = new MkGithub("comdor")
            .repos()
            .create(new RepoCreate("testrepo", false))
            .issues()
            .create("test issue", "test body");
        Mockito.when(command.issue()).thenReturn(issue);
        
        MatcherAssert.assertThat(
            issue.repo().labels().iterate(),
            Matchers.is(Matchers.emptyIterable())
        );
        
        final Create create = new Create(new Step.Fake(true));
        create.perform(command, log);
        
        MatcherAssert.assertThat(
            issue.repo().labels().iterate(),
            Matchers.iterableWithSize(4)
        );
    }
    
    /**
     * For {@link Create}. Some labels are actually specified, so they
     * should be created. However, if the command is ran a second time, no
     * duplicates are created.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void doesntCreateDuplicateLabels() throws Exception {
        final ComdorYaml yaml = Mockito.mock(ComdorYaml.class);
        Mockito.when(yaml.labels()).thenReturn(
            Arrays.asList(
                new String[]{
                    "0.0.1", "0.0.2", "@amihaiemil", "@comdor"
                }
            )
        );
        final Command command = Mockito.mock(Command.class);
        Mockito.when(command.comdorYaml()).thenReturn(yaml);
        final Log log = Mockito.mock(Log.class);
        Mockito.when(log.logger()).thenReturn(
            Mockito.mock(Logger.class)
        );
        final Issue issue = new MkGithub("comdor")
            .repos()
            .create(new RepoCreate("testrepo", false))
            .issues()
            .create("test issue", "test body");
        Mockito.when(command.issue()).thenReturn(issue);
        
        MatcherAssert.assertThat(
            issue.repo().labels().iterate(),
            Matchers.is(Matchers.emptyIterable())
        );
        
        final Create create = new Create(new Step.Fake(true));
        create.perform(command, log);
        MatcherAssert.assertThat(
            issue.repo().labels().iterate(),
            Matchers.iterableWithSize(4)
        );
        
        create.perform(command, log);
        MatcherAssert.assertThat(
            issue.repo().labels().iterate(),
            Matchers.iterableWithSize(4)
        );
    }
}
