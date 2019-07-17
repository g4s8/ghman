/*
 * Copyright (c) 2019 Kirill Ch. (g4s8.publci@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.g4s8.ghman.user;

import javax.json.Json;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.telegram.telegrambots.api.objects.Chat;

/**
 * Test for {@link UserDetails.FromChat}.
 * @since 1.0
 * @checkstyle JavadocVariableCheck (500 lines)
 * @checkstyle JavadocTypeCheck (500 lines)
 * @checkstyle ParameterNumberCheck (500 lines)
 */
final class UserDetailsFromChatTest {

    @Test
    void buildJsonFromChat() {
        final String fname = "James";
        final String lname = "Kirk";
        final String uname = "Jim";
        final long uid = 1024;
        new Assertion<>(
            "Return json with user details",
            new UserDetails.FromChat(new Fake(fname, lname, uname, uid)).details(),
            new IsEqual<>(
                Json.createObjectBuilder()
                    .add("firstName", fname)
                    .add("lastName", lname)
                    .add("userName", uname)
                    .add("uid", uid).build()
            )
        ).affirm();
    }

    private final class Fake extends Chat {

        private final String fname;

        private final String lname;

        private final String uname;

        private final long uid;

        private Fake(final String fname, final String lname,
            final String uname, final long uid) {
            this.fname = fname;
            this.lname = lname;
            this.uname = uname;
            this.uid = uid;
        }

        @Override
        public Long getId() {
            return this.uid;
        }

        @Override
        public String getFirstName() {
            return this.fname;
        }

        @Override
        public String getLastName() {
            return this.lname;
        }

        @Override
        public String getUserName() {
            return this.uname;
        }
    }
}
