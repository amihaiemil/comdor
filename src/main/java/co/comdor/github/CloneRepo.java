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

import co.comdor.Scripts;
import com.jcabi.github.Repo;

/**
 * Before running any other Scripts, comdor must clone the Repo and cd
 * into its root directory.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.3
 */
public final class CloneRepo implements Scripts {

    /**
     * Github repository.
     */
    private final Repo repo;

    /**
     * Scripts to run inside the Repo.
     */
    private final Scripts scripts;

    /**
     * Ctor.
     * @param repo Github repository.
     * @param scripts Scripts to run inside the repo.
     */
    public CloneRepo(final Repo repo, final Scripts scripts) {
        this.repo = repo;
        this.scripts = scripts;
    }

    @Override
    public String asText() {
        final String owner = this.repo.coordinates().user();
        final String name = this.repo.coordinates().repo();
        final StringBuilder clone = new StringBuilder();
        clone.append("git clone git@github.com:")
            .append(owner + "/" + name).append(".git").append("\n")
            .append("cd " + name).append("\n")
            .append("pwd").append("\n");
        return clone.append(this.scripts.asText()).append("\n").toString();
    }
}
