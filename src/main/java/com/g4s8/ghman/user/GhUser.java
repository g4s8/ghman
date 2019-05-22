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

import com.jcabi.github.Github;
import com.jcabi.http.response.JsonResponse;
import java.io.IOException;
import javax.json.JsonValue;
import org.cactoos.iterable.Mapped;

/**
 * Github user.
 *
 * @since 1.0
 */
public final class GhUser {

    /**
     * Github API.
     */
    private final Github github;

    /**
     * Ctor.
     * @param github Github API
     */
    public GhUser(final Github github) {
        this.github = github;
    }

    /**
     * Notification threads
     * @return Notifications
     * @throws IOException If fails
     */
    public Iterable<GhThread> notifications() throws IOException {
        return new Mapped<>(
            (JsonValue item) -> new GhThread(this.github, item.asJsonObject()),
            this.github.entry().uri().path("/notifications").back()
                .fetch()
                .as(JsonResponse.class)
                .json().readArray()
        );
    }

    /**
     * Get particular thread.
     * @param tid Thread id
     * @return Notification thread
     * @throws IOException If fails
     */
    public GhThread thread(final String tid) throws IOException {
        return new GhThread(
            this.github,
            this.github.entry()
                .uri()
                .path(String.format("/notifications/threads/%s", tid))
                .back()
                .fetch().as(JsonResponse.class)
                .json().readObject()
        );
    }
}
