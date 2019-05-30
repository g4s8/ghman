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
import javax.json.JsonObject;

/**
 * Github notification thread.
 * @since 1.0
 */
public final class GhThread implements Thread {

    /**
     * Json.
     */
    private final JsonObject jsn;

    /**
     * Ctor.
     * @param jsn Notification json
     */
    public GhThread(final JsonObject jsn) {
        this.jsn = jsn;
    }

    @Override
    public JsonObject subject() {
        return this.jsn.getJsonObject("subject");
    }

    @Override
    public String tid() {
        return this.jsn.getString("id");
    }

    @Override
    public Instant lastRead() {
        return Instant.parse(
            this.jsn.getString("last_read_at", Instant.EPOCH.toString())
        );
    }
}
