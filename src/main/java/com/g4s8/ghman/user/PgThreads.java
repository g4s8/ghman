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

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.sql.DataSource;

/**
 * Postgres threads.
 *
 * @since 1.0
 * @checkstyle MagicNumberCheck (500 lines)
 */
public final class PgThreads {

    /**
     * Data source.
     */
    private final DataSource data;

    /**
     * Ctor.
     * @param data Data source
     */
    public PgThreads(final DataSource data) {
        this.data = data;
    }

    /**
     * Find thread for user by id.
     * @param uid User id
     * @param tid Thread id
     * @return Thread
     */
    public Thread thread(final long uid, final String tid) {
        return new PgThreads.PgThread(this.data, uid, tid);
    }

    /**
     * Update thread.
     * @param uid User id
     * @param thread Thread
     * @throws IOException If fails
     */
    @SuppressWarnings("PMD.ExceptionAsFlowControl")
    public void update(final long uid, final Thread thread) throws IOException {
        try {
            new JdbcSession(this.data).autocommit(false)
                .sql("DELETE FROM threads WHERE uid = ? AND tid = ?")
                .set(uid).set(thread.tid())
                .execute()
                .clear()
                .sql(
                    String.join(
                        " ",
                        "INSERT INTO threads",
                        "(uid, tid, subject, last_read)",
                        "VALUES (?, ?, ?::json, ?)"
                    )
                )
                .prepare(
                    stmt -> {
                        try {
                            stmt.setLong(1, uid);
                            stmt.setString(2, thread.tid());
                            stmt.setString(3, thread.subject().toString());
                            stmt.setTimestamp(
                                4, Timestamp.from(thread.lastRead())
                            );
                        } catch (final IOException err) {
                            throw new SQLException(
                                "Failed to prepare statement", err
                            );
                        }
                    }
                ).execute().commit();
        } catch (final SQLException err) {
            throw new IOException("Failed to update thread", err);
        }
    }

    /**
     * Unread threads.
     * @return Unread threads map
     * @throws IOException If fails
     */
    public Map<Long, List<Thread>> unread() throws IOException {
        final JdbcSession session = new JdbcSession(this.data)
            .autocommit(false);
        final List<PgThread> threads;
        try {
            threads = session
                .sql("SELECT uid, tid FROM threads WHERE unread")
                .select(
                    new ListOutcome<>(
                        rset -> new PgThread(
                            this.data,
                            rset.getLong(1),
                            rset.getString(2)
                        )
                    )
                );
            session.sql("UPDATE threads SET unread = FALSE").execute()
                .commit();
        } catch (final SQLException err) {
            throw new IOException("Failed to select unread", err);
        }
        final Map<Long, List<Thread>> res = new HashMap<>();
        for (final PgThread thread : threads) {
            final List<Thread> users = res.getOrDefault(
                thread.uid, new LinkedList<>()
            );
            users.add(thread);
            res.put(thread.uid, users);
        }
        return res;
    }

    /**
     * Postgres thread implementation.
     * @since 1.0
     */
    private static final class PgThread implements Thread {

        /**
         * Data source.
         */
        private final DataSource data;

        /**
         * User id.
         */
        private final long uid;

        /**
         * Thread id.
         */
        private final String thrd;

        /**
         * Ctor.
         * @param data Data source
         * @param uid User id
         * @param tid Thread id
         */
        PgThread(final DataSource data,
            final long uid, final String tid) {
            this.data = data;
            this.uid = uid;
            this.thrd = tid;
        }

        @Override
        public JsonObject subject() throws IOException {
            try {
                return new JdbcSession(this.data).sql(
                    "SELECT title FROM threads WHERE uid = ? AND tid = ?"
                ).set(this.uid).set(this.thrd).select(
                    (rset, stmt) -> {
                        if (!rset.next()) {
                            throw new SQLException("Empty subject");
                        }
                        return Json.createReader(
                            new StringReader(rset.getString(1))
                        ).readObject();
                    }
                );
            } catch (final SQLException err) {
                throw new IOException("Failed to select title", err);
            }
        }

        @Override
        public String tid() {
            return this.thrd;
        }

        @Override
        public Instant lastRead() throws IOException {
            try {
                return new JdbcSession(this.data).sql(
                    "SELECT last_read FROM threads WHERE uid = ? AND tid = ?"
                ).set(this.uid).set(this.thrd)
                    .select(
                        (rset, stmt) -> {
                            if (!rset.next()) {
                                throw new SQLException("Empty last_read");
                            }
                            return rset.getTimestamp(1).toInstant();
                        }
                    );
            } catch (final SQLException err) {
                throw new IOException("Failed to select last_read", err);
            }
        }
    }
}
