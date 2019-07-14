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

import com.g4s8.teletakes.rs.TmResponse;
import com.g4s8.teletakes.tk.TmException;
import com.g4s8.teletakes.tk.TmTake;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Reply to a notification.
 *
 * @since 1.0
 * @todo #10:30min Continue implementing this class. The objective is
 *  to answer to the `/reply` command in order to post a comment in
 *  the github thread.
 */
public final class TkReply implements TmTake {

    /**
     * Command pattern.
     */
    private static final Pattern PTN_CMD =
        Pattern.compile("/reply (?<tid>.*) (?<reply>.*)");

    @Override
    public TmResponse act(final Update upd) throws IOException {
        final Matcher matcher = PTN_CMD.matcher(upd.getMessage().getText());
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                new UncheckedText(
                    new FormattedText(
                        "Illegal request: %s", upd.getMessage().getText()
                    )
                ).asString()
            );
        }
        throw new TmException(
            new UncheckedText(
                new FormattedText("Unknown notification '%s'", matcher.group("tid"))
            ).asString()
        );
    }
}
