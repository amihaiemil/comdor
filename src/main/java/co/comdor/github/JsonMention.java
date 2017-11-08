/**
 * Copyright (c) 2017, Mihai Emil Andronache
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1)Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  2)Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *  3)Neither the name of comdor nor the names of its
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

import com.jcabi.github.Content;
import com.jcabi.github.Issue;
import java.io.ByteArrayInputStream;

import javax.json.JsonObject;
import java.io.IOException;

/**
 * {@link Mention} represented in Json.<br><br>
 * This class is abstract because we don't know <b>which</b> comment
 * is this? We are interested in the <b>last</b> mentioning comment, so
 * this class is extended by {@link LastMention}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #9:30min Implement method scripts(), which extracts the code
 *  to run from the comment, and write some unit tests for it.
 */
public abstract class JsonMention implements Mention {

    /**
     * Github issue Comment in Json format.
     * @see https://developer.github.com/v3/issues/comments/
     */
    private final JsonObject json;

    /**
     * Issue where the mention was found.
     */
    private final Issue issue;

    /**
     * Ctor.
     * @param json Github Comment in Json.
     * @param issue Github Issue.
     */
    public JsonMention(final JsonObject json, final Issue issue) {
        this.json = json;
        this.issue = issue;
    }

    @Override
    public abstract String type();

    @Override
    public abstract Language language();

    @Override
    public final ComdorYaml comdorYaml() throws IOException {
        final ComdorYaml yaml;
        if(this.issue.repo().contents().exists(".comdor.yml", "master")) {
            yaml = new ComdorYamlRules(
                new ComdorYamlInput(
                    new ByteArrayInputStream(
                        new Content.Smart(
                            this.issue.repo()
                                .contents()
                                .get(".comdor.yml")
                        ).decoded()
                    )
                )
            );
        } else {
            yaml = new ComdorYaml.Missing();
        }
        return yaml;
    }
    
    @Override
    public abstract void understand(final Language... langs) throws IOException;

    @Override
    public final String author() {
        return this.json.getJsonObject("user").getString("login");
    }


    @Override
    public final String scripts() {
        return "echo 'hello world'";
    }

    @Override
    public final Issue issue() {
        return this.issue;
    }

    @Override
    public final void reply(final String message) throws IOException {
        final String reply = "@" + this.author() + " " + message;
        this.issue.comments().post(reply);
    }

    @Override
    public final JsonObject json(){
        return this.json;
    }


}
