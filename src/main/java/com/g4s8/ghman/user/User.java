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

import com.jcabi.github.mock.MkGithub;
import java.io.IOException;
import javax.json.JsonObject;

/**
 * User.
 * @since 1.0
 */
public interface User {
    /**
     * Github user.
     * @return User
     * @throws GhAuthException If not authorized in Github
     * @throws IOException If fails
     */
    GhUser github() throws GhAuthException, IOException;

    /**
     * Authorize user with Github token.
     * @param token Token
     * @throws IOException If fails
     */
    void authorize(String token) throws IOException;

    /**
     * Telegram data.
     * @return Telegram data
     * @throws IOException If fails
     */
    JsonObject telegram() throws IOException;

    /**
     * User id.
     * @return Id
     */
    long uid();

    /**
     * Telegram identifier.
     * @return Telegram id
     * @throws IOException When smth wrong
     */
    String tid() throws IOException;

    final class Fake implements User {

        @Override
        public GhUser github() throws GhAuthException, IOException {
            return new GhUser(new MkGithub());
        }

        @Override
        public void authorize(final String token) throws IOException {

        }

        @Override
        public JsonObject telegram() throws IOException {
            return null;
        }

        @Override
        public long uid() {
            return 0;
        }

        @Override
        public String tid() throws IOException {
            return null;
        }
    }
}
