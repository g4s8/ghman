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
package com.g4s8.ghman.web;

import com.g4s8.ghman.user.PgThreads;
import com.g4s8.ghman.user.Thread;
import com.g4s8.ghman.user.User;
import com.g4s8.ghman.user.Users;
import java.io.IOException;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsEmpty;

/**
 * Synchronization trigger, called by Heroku scheduler every 10 minutes.
 * <p>
 *     This take synchronizes all unread threads for active users and
 *     marks these threads as unread with updating last_read value.
 * </p>
 * @since 1.0
 */
public final class TkSync implements Take {

    /**
     * Data source.
     */
    private final DataSource data;

    /**
     * Ctor.
     * @param data Data source
     */
    public TkSync(final DataSource data) {
        this.data = data;
    }

    @Override
    public Response act(final Request req) throws IOException {
        final PgThreads tds = new PgThreads(this.data);
        for (final User user : new Users(this.data).active()) {
            for (final Thread thread : user.github().notifications()) {
                final Thread copy = tds.thread(user.uid(), thread.tid());
                if (copy.lastRead().isBefore(thread.lastRead())) {
                    tds.update(user.uid(), thread);
                }
            }
        }
        return new RsEmpty();
    }
}
