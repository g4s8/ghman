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

import java.time.Instant;
import javax.json.Json;
import javax.json.JsonObject;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;

/**
 * Test case for {@link GhThread}.
 *
 * @since 1.0
 * @checkstyle JavadocMethodCheck (500 lines)
 */
public final class GhThreadTest {

    @Test
    void parseId() {
        final String tid = "4";
        new Assertion<>(
            "Failed to get thread id from json",
            new GhThread(
                Json.createObjectBuilder().add("id", tid).build()
            ).tid(),
            new IsEqual<>(tid)
        ).affirm();
    }

    @Test
    void returnsSubject() {
        final JsonObject subj = Json.createObjectBuilder()
            .add("title", "issue #3").add("link", "http://issues/3").build();
        new Assertion<>(
            "Failed to get subject from json",
            new GhThread(
                Json.createObjectBuilder().add("subject", subj).build()
            ).subject(),
            new IsEqual<>(subj)
        ).affirm();
    }

    @Test
    void returnsLastRead() {
        final String date = "2019-12-03T10:15:30.00Z";
        new Assertion<>(
            "Failed to get last read from json",
            new GhThread(
                Json.createObjectBuilder().add("last_read_at", date).build()
            ).lastRead(),
            new IsEqual<>(Instant.parse(date))
        ).affirm();
    }

    @Test
    void returnsEpochWhenLastReadIsAbsent() {
        new Assertion<>(
            "Failed to get Instant.EPOCH as last read",
            new GhThread(
                Json.createObjectBuilder().build()
            ).lastRead(),
            new IsEqual<>(Instant.EPOCH)
        ).affirm();
    }
}
