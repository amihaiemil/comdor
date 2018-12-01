/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.comdor.github;

import com.amihaiemil.camel.Yaml;
import com.amihaiemil.camel.YamlMapping;
import com.amihaiemil.camel.YamlSequence;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ComdorYaml from InputStream.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.2
 */
public final class ComdorYamlInput implements ComdorYaml {

    /**
     * Contents of .comdor.yml.
     */
    private final YamlMapping yaml;

    /**
     * Ctor.
     * @param yaml .charles.yml.
     * @throws IOException If the input stream cannot be read.
     */
    public ComdorYamlInput(final InputStream yaml) throws IOException {
        this.yaml = Yaml.createYamlInput(yaml).readYamlMapping();
    }
    
    @Override
    public String docker() {
        return this.yaml.string("docker");
    }

    @Override
    public List<String> architects() {
        final List<String> architects = new ArrayList<>();
        final YamlSequence sequence = this.yaml.yamlSequence("architects");
        if(sequence != null) {
            for(int idx=0; idx<sequence.size(); idx = idx + 1) {
                architects.add(sequence.string(idx));
            }
        }
        return architects;
    }

    @Override
    public String taggedArchitects() {
        final StringBuilder tagged = new StringBuilder();
        final List<String> architects = this.architects();
        for(int idx = 0; idx < architects.size(); idx = idx + 1) {
            if(architects.size() > 1 && idx == architects.size() - 1) {
                tagged.append("or ");
            }
            tagged.append("@" + architects.get(idx)).append(" ");
        }
        return tagged.toString().trim();
    }

    @Override
    public List<String> commanders() {
        final List<String> commanders = new ArrayList<>();
        final YamlSequence sequence = this.yaml.yamlSequence("commanders");
        if(sequence != null) {
            for(int idx=0; idx<sequence.size(); idx = idx + 1) {
                commanders.add(sequence.string(idx));
            }
        }
        return commanders;
    }

    @Override
    public List<String> labels() {
        final List<String> labels = new ArrayList<>();
        final YamlSequence sequence = this.yaml.yamlSequence("labels");
        if(sequence != null) {
            for(int idx=0; idx<sequence.size(); idx = idx + 1) {
                labels.add(sequence.string(idx));
            }
        }
        return labels;
    }
}
