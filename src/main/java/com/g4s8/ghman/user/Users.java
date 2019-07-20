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

import com.jcabi.github.Github;
import java.io.IOException;
import org.cactoos.iterable.IterableOf;
import org.telegram.telegrambots.api.objects.Chat;

/**
 * Users.
 * @since 1.0
 */
public interface Users {
    /**
     * User for telegram chat.
     *
     * @param chat Chat
     * @return User
     * @throws IOException If fails
     */
    User user(Chat chat) throws IOException;

    /**
     * User by id.
     * @param uid User id
     * @return User
     */
    User user(long uid);

    /**
     * Active users authenticated with Github.
     * @return Users list
     * @throws IOException If fails
     */
    Iterable<User> active() throws IOException;

    /**
     * Fake implementation for tests.
     * @since 1.0
     */
    final class Fake implements Users {

        /**
         * Users.
         */
        private final Iterable<User> users;

        /**
         * Ctor.
         * @param github Github api
         */
        public Fake(final Github github) {
            this(new User.Fake(github));
        }

        /**
         * Ctor.
         * @param users Users
         */
        public Fake(final User... users) {
            this(new IterableOf<>(users));
        }

        /**
         * Ctor.
         * @param users Users
         */
        public Fake(final Iterable<User> users) {
            this.users = users;
        }

        @Override
        public User user(final Chat chat) throws IOException {
            return this.users.iterator().next();
        }

        @Override
        public User user(final long uid) {
            return this.users.iterator().next();
        }

        @Override
        public Iterable<User> active() throws IOException {
            return this.users;
        }
    }
}
