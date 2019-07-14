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

import com.g4s8.teletakes.tk.TmException;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.Throws;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Test for {@link TkReply}.
 * @since 1.0
 * @checkstyle JavadocTypeCheck (500 lines)
 * @checkstyle JavadocVariableCheck (500 lines)
 */
public final class TkReplyTest {

    @Test
    public void cantReplyToNonexistingNotification() throws Exception {
        final String id = "nope";
        new Assertion<>(
            "can't reply to non-existing notification",
            () -> new TkReply().act(new ReplyUpdate(id, "something")),
            new Throws<>(
                new FormattedText("Unknown notification '%s'", id).asString(),
                TmException.class
            )
        ).affirm();
    }

    @SuppressWarnings("serial")
    private static final class ReplyUpdate extends Update {

        private final String tid;

        private final String reply;

        ReplyUpdate(final String tid, final String reply) {
            this.tid = tid;
            this.reply = reply;
        }

        @Override
        public boolean hasMessage() {
            return true;
        }

        @Override
        public Message getMessage() {
            return new Message() {
                @Override
                public boolean hasText() {
                    return true;
                }

                @Override
                public String getText() {
                    return new UncheckedText(
                        new FormattedText(
                            "/reply %s %s",
                            ReplyUpdate.this.tid,
                            ReplyUpdate.this.reply
                        )
                    ).asString();
                }
            };
        }
    }
}
