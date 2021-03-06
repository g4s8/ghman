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

import com.g4s8.ghman.user.Thread;
import com.g4s8.ghman.user.Threads;
import com.g4s8.ghman.user.User;
import com.g4s8.ghman.user.Users;
import com.g4s8.teletakes.bot.Bot;
import com.jcabi.github.mock.MkGithub;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.cactoos.iterable.IterableOf;
import org.hamcrest.core.AllOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasSize;
import org.llorllale.cactoos.matchers.HasValuesMatching;
import org.telegram.telegrambots.api.methods.send.SendMessage;

/**
 * Test for {@link UnreadThreadsSync}.
 * @since 1.0
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle JavadocTypeCheck (500 lines)
 * @checkstyle JavadocVariableCheck (500 lines)
 */
final class UnreadThreadsSyncTest {

    @Test
    void notifiesUser() throws IOException {
        final String tid = "telegram_id";
        final FakeBot bot = new FakeBot();
        new UnreadThreadsSync(
            bot,
            new Threads.Fake(1L, new Thread.Fake("1")),
            new Users.Fake(new User.Fake(new MkGithub(), tid))
        ).sync();
        new Assertion<>(
            "must have notified user",
            bot.sent,
            new AllOf<>(
                new IterableOf<>(
                    new HasSize(1),
                    new HasValuesMatching<>(msg -> msg.getChatId().equals(tid))
                )
            )
        ).affirm();
    }

    private static final class FakeBot implements Bot {

        private final List<SendMessage> sent = new LinkedList<>();

        @Override
        public void send(final SendMessage msg) {
            this.sent.add(msg);
        }
    }
}
