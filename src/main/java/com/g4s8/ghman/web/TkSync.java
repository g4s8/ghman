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

import java.io.IOException;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsEmpty;

/**
 * Synchronization trigger, called by Heroku scheduler every 10 minutes.
 * @since 1.0
 * @todo #26:30 In case `Sync.sync` throws an exception, this class should log
 *  it instead of failing the whole synchronization process. Some thought must
 *  be taken to apply the same principle to instances of `Sync` in case they do
 *  multiple tasks.
 */
public final class TkSync implements Take {

    /**
     * Syncs.
     */
    private final Iterable<Sync> syncs;

    /**
     * Ctor.
     * @param syncs Syncs
     */
    public TkSync(final Iterable<Sync> syncs) {
        this.syncs = syncs;
    }

    @Override
    public Response act(final Request req) throws IOException {
        for (final Sync sync : this.syncs) {
            sync.sync();
        }
        return new RsEmpty();
    }

    /**
     * Sync.
     * @since 1.0
     */
    public interface Sync {
        /**
         * Synchronize some state.
         * @throws IOException If an error happens
         */
        void sync() throws IOException;
    }
}
