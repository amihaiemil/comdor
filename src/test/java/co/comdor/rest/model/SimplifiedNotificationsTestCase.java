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
package co.comdor.rest.model;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.json.Json;

/**
 * Unit tests for {@link SimplifiedNotifications}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
@SuppressWarnings("unused")
public final class SimplifiedNotificationsTestCase {

    
    /**
     * SimplifiedNotifications can iterate over all of them.
     */
    @Test
    public void iteratesOverAll() {
        final Notifications notifs = new SimplifiedNotifications(
            Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("repoFullName", "jeff/test").add("issueNumber", 123).build())
                .add(Json.createObjectBuilder().add("repoFullName", "mary/tesla").add("issueNumber", 458).build())
                .add(Json.createObjectBuilder().add("repoFullName", "john/doe-repo").add("issueNumber", 142).build())
                .build()
                .toString()
        );
        int size = 0;
        for(final Notification n : notifs) {
            size++; 
        }
        MatcherAssert.assertThat(size, Matchers.is(3));
    }
    
    /**
     * SimplifiedNotifications can iterate over a single notification.
     */
    @Test
    public void iteratesOverOne() {
        final Notifications notifs = new SimplifiedNotifications(
            Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("repoFullName", "mary/tesla").add("issueNumber", 458).build())
                .build()
                .toString()
        );
        int size = 0;
        for(final Notification n : notifs) {
            size++; 
        }
        MatcherAssert.assertThat(size, Matchers.is(1));
    }
    
    /**
     * SimplifiedNotifications can iterate over no notification (it is empty).
     */
    @Test
    public void iteratesOverNone() {
        final Notifications notifs = new SimplifiedNotifications(
            Json.createArrayBuilder()
                .build()
                .toString()
        );
        int size = 0;
        for(final Notification n : notifs) {
            size++; 
        }
        MatcherAssert.assertThat(size, Matchers.is(0));
    }
    
}
