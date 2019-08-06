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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.cactoos.list.ListOf;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;

/**
 * Github notifications thread.
 *
 * @since 1.0
 */
public interface Threads {

    /**
     * Find thread for user by id.
     * @param uid User id
     * @param tid Thread id
     * @return Thread
     */
    Thread thread(long uid, String tid);

    /**
     * Update thread.
     * @param uid User id
     * @param thread Thread
     * @throws IOException If fails
     */
    void update(long uid, Thread thread) throws IOException;

    /**
     * Unread threads.
     * @return User id to unread threads map
     * @throws IOException If fails
     */
    Map<Long, List<Thread>> unread() throws IOException;

    /**
     * Fake.
     * @since 1.0
     */
    final class Fake implements Threads {

        /**
         * Threads by user id.
         */
        private final Map<Long, List<Thread>> threads;

        /**
         * Ctor.
         * @param uid User id.
         * @param thread Thread.
         */
        public Fake(final long uid, final Thread thread) {
            this(uid, new ListOf<>(thread));
        }

        /**
         * Ctor.
         * @param uid User id.
         * @param threads Threads.
         */
        public Fake(final long uid, final List<Thread> threads) {
            this(new MapOf<>(new MapEntry<>(uid, threads)));
        }

        /**
         * Ctor.
         * @param threads Threads by user id.
         */
        public Fake(final Map<Long, List<Thread>> threads) {
            this.threads = threads;
        }

        @Override
        public Thread thread(final long uid, final String tid) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void update(final long uid, final Thread thread) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<Long, List<Thread>> unread() throws IOException {
            return this.threads;
        }
    }
}
