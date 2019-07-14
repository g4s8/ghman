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

import com.g4s8.ghman.web.TkSync;
import java.io.IOException;

/**
 * Synchronization trigger for {@link Threads}.
 * <p>
 *     This take synchronizes all unread threads for active users and
 *     marks these threads as unread with updating last_read value.
 * </p>
 * @since 1.0
 */
public final class ThreadsSync implements TkSync.Sync {

    /**
     * Threads.
     */
    private final Threads threads;

    /**
     * Users.
     */
    private final Users users;

    /**
     * Ctor.
     * @param threads Threads.
     * @param users Users.
     */
    public ThreadsSync(final Threads threads, final Users users) {
        this.threads = threads;
        this.users = users;
    }

    @Override
    public void sync() throws IOException {
        for (final User user : this.users.active()) {
            for (final Thread thread : user.github().notifications()) {
                final Thread copy = this.threads.thread(
                    user.uid(), thread.tid()
                );
                if (copy.lastRead().isBefore(thread.lastRead())) {
                    this.threads.update(user.uid(), thread);
                }
            }
        }
    }
}
