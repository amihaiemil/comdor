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

import java.util.List;

/**
 * Decorator over a ComdorYaml object, which applies some rules to it.
 * E.g. Gives it a default docker image or apply other logics to the returned
 * contents.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.2
 */
public final class ComdorYamlRules implements ComdorYaml {

    /**
     * Decorated instance.
     */
    private ComdorYaml decorated;
    
    /**
     * Ctor.
     * @param decorated ComdorYaml instance to which these rules apply.
     */
    public ComdorYamlRules(final ComdorYaml decorated) {
        this.decorated = decorated;
    }
    
    /**
     * If a docker image to use is not provided, use a default one.
     * @return String docker image name.
     */
    @Override
    public String docker() {
        String image = this.decorated.docker();
        if(image == null || image.isEmpty()) {
            image = "amihaiemil/comdor";
        }
        return image;
    }

    @Override
    public List<String> architects() {
        return this.decorated.architects();
    }

    @Override
    public String taggedArchitects() {
        return this.decorated.taggedArchitects();
    }

    @Override
    public List<String> commanders() {
        return this.decorated.commanders();
    }
    
}
