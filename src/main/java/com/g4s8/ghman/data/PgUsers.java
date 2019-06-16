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
package com.g4s8.ghman.data;

import com.g4s8.ghman.user.User;
import com.g4s8.ghman.user.Users;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import java.io.IOException;
import java.sql.SQLException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.sql.DataSource;
import org.cactoos.Scalar;
import org.cactoos.text.Joined;
import org.telegram.telegrambots.api.objects.Chat;

/**
 * Postgres {@link Users} implementation.
 *
 * @since 1.0
 * @todo #7:30min Create integration test: check all methods of this class and
 *  do not forget about all possible exceptions.
 */
public final class PgUsers implements Users {

    /**
     * Data source.
     */
    private final DataSource data;

    /**
     * Ctor.
     * @param data Data source
     */
    public PgUsers(final DataSource data) {
        this.data = data;
    }

    @Override
    public User user(final Chat chat) throws IOException {
        final JsonObject details = new JsonUserDetails(chat).value();
        try {
            return new PgUser(
                this.data,
                new JdbcSession(this.data)
                    .sql(
                        new Joined(
                            " ",
                            "INSERT INTO users (tid, tname)",
                            "VALUES (?, ?::json)",
                            "ON CONFLICT (tid) DO UPDATE SET tname = ?::json",
                            "RETURNING uid"
                        ).asString()
                    )
                    .set(chat.getId())
                    .set(details.toString())
                    .set(details.toString())
                    .select(new SingleOutcome<>(Long.class))
            );
        } catch (final SQLException err) {
            throw new IOException("Failed to select user", err);
        }
    }

    @Override
    public User user(final long uid) {
        return new PgUser(this.data, uid);
    }

    @Override
    public Iterable<User> active() throws IOException {
        try {
            return new JdbcSession(this.data).sql(
                "SELECT uid FROM users WHERE gh_token != ''"
            ).select(
                new ListOutcome<>(
                    rset -> new PgUser(this.data, rset.getLong(1))
                )
            );
        } catch (final SQLException err) {
            throw new IOException("Failed to select active users", err);
        }
    }

    /**
     * User details as json from telegram chat.
     *
     * @since 1.0
     * @todo #18:30min This class represents details about a user stored in
     *  the database. Callers of PgUser.telegram relies on the datastructure
     *  defined here and must know how it is structured to exploit the
     *  information. Instead, extract interface from it and use it
     *  everywhere it is needed instead of exposing this data. One
     *  implementation is maybe based on Chat and another would be based on
     *  a pg request for example. Add unit tests for both when it's done.
     */
    private static final class JsonUserDetails implements Scalar<JsonObject> {

        /**
         * Telegram chat with a user.
         */
        private final Chat chat;

        /**
         * Ctor.
         * @param chat Telegram chat with a user
         */
        JsonUserDetails(final Chat chat) {
            this.chat = chat;
        }

        @Override
        public JsonObject value() {
            final JsonObjectBuilder details = Json.createObjectBuilder()
                .add("firstName", this.chat.getFirstName());
            if (this.chat.getLastName() != null) {
                details.add("lastName", this.chat.getLastName());
            }
            if (this.chat.getUserName() != null) {
                details.add("userName", this.chat.getUserName());
            }
            details.add("uid", this.chat.getId());
            return details.build();
        }
    }
}
