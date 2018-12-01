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

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration file present in the Github repository.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.2
 * @todo #27:30min Design and implement "aliases", which will basically be
 *  customized commands, aliases, labels for scripts which the users may want
 *  to execute periodically, like merging or deploying scripts.
 */
public interface ComdorYaml {
    
    /**
     * Docker image to use.
     * @return String.
     */
    String docker();
    
    /**
     * Architects of the project. Users who can command the bot without any
     * questions asked.
     * @return List String of architects.
     */
    List<String> architects();

    /**
     * Returns the tagged architects (e.g. "@john @mary or @joe)
     * @return String containing all the tagged architects.
     */
    String taggedArchitects();
    
    /**
     * Commanders. Users who can command the bot, but the bot will ask one of
     * the architects to confirm.
     * @return List String of commanders.
     */
    List<String> commanders();

    /**
     * Labels to be created in the repo.
     * @return List String of labels.
     */
    List<String> labels();

    /**
     * Missing .comdor.yml file.
     */
    final class Missing implements ComdorYaml {

        @Override
        public String docker() {
            return "";
        }

        @Override
        public List<String> architects() {
            return new ArrayList<>();
        }

        @Override
        public String taggedArchitects() {
            return "";
        }

        @Override
        public List<String> commanders() {
            return new ArrayList<>();
        }

        @Override
        public List<String> labels() {
            return new ArrayList<>();
        }

    }
    
}
