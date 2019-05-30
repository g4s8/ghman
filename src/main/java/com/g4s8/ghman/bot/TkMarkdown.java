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
package com.g4s8.ghman.bot;

import com.g4s8.teletakes.rs.RsDirectives;
import com.g4s8.teletakes.rs.TmResponse;
import com.g4s8.teletakes.tk.TmTake;
import java.io.IOException;
import org.telegram.telegrambots.api.objects.Update;
import org.xembly.Directives;

/**
 * Use markdown for all responses.
 *
 * @since 1.0
 * @todo #1:30min Move this class to g4s8/teletakes library:
 *  submit PR, merge it and update dependency then.
 */
final class TkMarkdown implements TmTake {

    /**
     * Origin take.
     */
    private final TmTake origin;

    /**
     * Wrap origin take.
     *
     * @param origin Take
     */
    TkMarkdown(final TmTake origin) {
        this.origin = origin;
    }

    @Override
    public TmResponse act(final Update req) throws IOException {
        final TmResponse act = this.origin.act(req);
        return new RsDirectives(
            act,
            new Directives()
                .xpath("/response/message/text")
                .attr("kind", "markdown")
        );
    }
}
