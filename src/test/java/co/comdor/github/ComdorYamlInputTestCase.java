/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.comdor.github;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit tests for {@link ComdorYamlInput}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.2
 */
public final class ComdorYamlInputTestCase {
    
    /**
     * ComdorYamlInput can read the commanders list.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void readsCommanders() throws IOException {
        final ComdorYaml comdor = new ComdorYamlInput(
            new ByteArrayInputStream(
                "docker: a/b\ncommanders:\n  - john\n  - amihaiemil".getBytes()
            )
        );
        final List<String> commanders = comdor.commanders();
        MatcherAssert.assertThat(commanders, Matchers.hasSize(2));
        MatcherAssert.assertThat(
            commanders.get(0), Matchers.equalTo("amihaiemil")
        );
        MatcherAssert.assertThat(
            commanders.get(1), Matchers.equalTo("john")
        );
    }
    
    /**
     * ComdorYamlInput can read the architects list.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void readsArchitects() throws IOException {
        final ComdorYaml comdor = new ComdorYamlInput(
            new ByteArrayInputStream(
                "docker: c/d\narchitects:\n  - john\n  - amihaiemil".getBytes()
            )
        );
        final List<String> architects = comdor.architects();
        MatcherAssert.assertThat(architects, Matchers.hasSize(2));
        MatcherAssert.assertThat(
            architects.get(0), Matchers.equalTo("amihaiemil")
        );
        MatcherAssert.assertThat(
            architects.get(1), Matchers.equalTo("john")
        );
    }

    /**
     * ComdorYamlInput can read the labels list.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void readsLabels() throws IOException {
        final ComdorYaml comdor = new ComdorYamlInput(
            new ByteArrayInputStream(
                "docker: c/d\nlabels:\n  - 0.0.1\n  - 0.0.2".getBytes()
            )
        );
        final List<String> labels = comdor.labels();
        MatcherAssert.assertThat(labels, Matchers.hasSize(2));
        MatcherAssert.assertThat(
            labels.get(0), Matchers.equalTo("0.0.1")
        );
        MatcherAssert.assertThat(
            labels.get(1), Matchers.equalTo("0.0.2")
        );
    }

    /**
     * ComdorYamlInput can tag the architects.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void tagsArchitects() throws IOException {
        final ComdorYaml comdor = new ComdorYamlInput(
            new ByteArrayInputStream(
                "docker: c/d\narchitects:\n  - joe\n  - emil".getBytes()
            )
        );
        final String tagged = comdor.taggedArchitects();
        MatcherAssert.assertThat(tagged, Matchers.equalTo("@emil or @joe"));
    }

    /**
     * Reads the docker attribute.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void readsDocker() throws IOException {
        final ComdorYaml comdor = new ComdorYamlInput(
            new ByteArrayInputStream(
                "docker: amihaiemil/java9".getBytes()
            )
        );
        MatcherAssert.assertThat(
            comdor.docker(), Matchers.equalTo("amihaiemil/java9")
        );
    }
    
    /**
     * Docker attribute is missing, so ComdorYamlInput returns null.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void missingDocker() throws IOException {
        final ComdorYaml comdor = new ComdorYamlInput(
            new ByteArrayInputStream(
                "commanders:\n  - somone".getBytes()
            )
        );
        MatcherAssert.assertThat(
            comdor.docker(), Matchers.equalTo(null)
        );
    }
    
    /**
     * The commanders' list is missing, so ComdorYamlInput returns an
     * empty list.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void missingCommanders() throws IOException {
        final ComdorYaml comdor = new ComdorYamlInput(
            new ByteArrayInputStream(
                "architects:\n  - somone".getBytes()
            )
        );
        MatcherAssert.assertThat(
            comdor.commanders(), Matchers.iterableWithSize(0)
        );
    }
    
    /**
     * The architects' list is missing, so ComdorYamlInput returns an
     * empty list.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void missingArchitects() throws IOException {
        final ComdorYaml comdor = new ComdorYamlInput(
            new ByteArrayInputStream(
                "docker: a/b".getBytes()
            )
        );
        MatcherAssert.assertThat(
            comdor.architects(), Matchers.iterableWithSize(0)
        );
    }

    /**
     * The labels' list is missing, so ComdorYamlInput returns an
     * empty list.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void missingLabels() throws IOException {
        final ComdorYaml comdor = new ComdorYamlInput(
            new ByteArrayInputStream(
                "docker: a/b".getBytes()
            )
        );
        MatcherAssert.assertThat(
            comdor.labels(), Matchers.iterableWithSize(0)
        );
    }
}
