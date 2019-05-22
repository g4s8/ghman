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
 *
 *
 */

package com.g4s8.ghman.user;

import com.g4s8.ghman.user.GhAuthException;
import com.g4s8.ghman.user.GhUser;
import com.jcabi.github.RtGithub;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * User.
 *
 * @since 1.0
 */
public final class User {

    /**
     * Data source.
     */
    private final DataSource data;

    /**
     * User id.
     */
    private final long id;

    /**
     * Ctor.
     * @param data Data source
     * @param uid User id
     */
    public User(final DataSource data, final long uid) {
        this.data = data;
        this.id = uid;
    }

    /**
     * Github user.
     * @return User
     * @throws GhAuthException If not authorized in Github
     * @throws IOException If fails
     */
    public GhUser github() throws GhAuthException, IOException {
        final String token;
        try {
            token = new JdbcSession(this.data).sql(
                "SELECT gh_token FROM users WHERE uid = ?"
            ).set(this.id).select(new SingleOutcome<>(String.class));
        } catch (final SQLException err) {
            throw new IOException("Failed to select token", err);
        }
        if (token.isEmpty()) {
            throw new GhAuthException("Github token was not found");
        }
        return new GhUser(new RtGithub(token));
    }

    /**
     * Authorize user with Github token.
     * @param token Token
     * @throws IOException If fails
     */
    public void authorize(final String token) throws IOException {
        try {
            new JdbcSession(this.data).sql(
                "UPDATE users SET gh_token = ? WHERE uid = ?"
            ).set(token).set(this.id).update(Outcome.VOID);
        } catch (final SQLException err) {
            throw new IOException("Failed to update token", err);
        }
    }

    /**
     * User id.
     * @return Id
     */
    public long uid() {
        return this.id;
    }
}
