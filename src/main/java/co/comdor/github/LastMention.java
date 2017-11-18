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

import com.google.common.collect.Lists;
import com.jcabi.github.Comment;
import com.jcabi.github.Issue;
import javax.json.JsonObject;
import java.io.IOException;
import java.util.List;

/**
 * Last comment on a Github issue, where the bot has been mentioned.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class LastMention extends JsonMention {

    /**
     * What type of mention is it? "hello", "run" etc.
     * Initially, this is unknown.
     */
    private String type = "unknown";

    /**
     * What language is spoken? Default is English.
     */
    private Language lang = new English();

    /**
     * Ctor.
     * @param issue Github issue.
     * @throws IOException If something goes wrong with the HTTP calls.
     */
    public LastMention(final Issue issue) throws IOException {
        super(LastMention.findLastMention(issue), issue);
    }

    @Override
    public String type() {
        return this.type;
    }

    @Override
    public Language language() {
        return this.lang;
    }

    @Override
    public void understand(final Language... langs) throws IOException {
        for(final Language spoken : langs) {
            this.type = spoken.categorize(this);
            if(!"unknown".equals(this.type)) {
                this.lang = spoken;
                break;
            }
        }
    }

    /**
     * Looks for the last mentioning comment in this Github issue.
     * It searches for the most recent (bottoms up).
     * @param issue Github Issue.
     * @return JsonObject representing the Github comment as it is returned
     *  by the API.
     * @see https://developer.github.com/v3/issues/comments/
     * @throws IOException If something goes wrong with the HTTP calls.
     */
    private static JsonObject findLastMention(
        final Issue issue
    ) throws IOException {
        final List<Comment> comments = Lists.newArrayList(
            issue.comments().iterate()
        );
        final String agentLogin = issue.repo().github().users().self().login();
        for(int idx=comments.size() - 1; idx >=0; idx = idx-1) {
            final JsonObject current = comments.get(idx).json();
            final boolean foundReply = agentLogin.equalsIgnoreCase(
                current.getJsonObject("user").getString("login")
            );
            if(foundReply) {
                throw new MentionLookupException(
                    "Last mention is already answered!"
                );
            } else {
                if(current.getString("body").contains("@" + agentLogin)) {
                    return current;
                }
            }
        }
        throw new MentionLookupException("No mention found!");
    }
}
