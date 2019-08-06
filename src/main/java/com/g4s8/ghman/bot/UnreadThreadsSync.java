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
import com.g4s8.ghman.user.Users;
import com.g4s8.ghman.web.Sync;
import com.g4s8.teletakes.bot.Bot;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.telegram.telegrambots.api.methods.send.SendMessage;

/**
 * Notify user into telegram chat.
 * @since 1.0
 * @todo #29:30min Instantiate this class and pass it to `TkSync`
 *  (currently in `TkApp`) so that the behaviour of this class is
 *  executed every 10 minutes when Heroku ntofify the app. Note
 *  that this involves sharing the instance of BotSimple between
 *  `TkApp` and `BotApp`.
 * @todo #9:30min Now this class send only github subject title as
 *  notification. Please aks ARC what exactly we want to send to user and
 *  create a class to extract and format this information from `JsonObject`
 *  (github subject) accordingly.
 */
public final class UnreadThreadsSync implements Sync {

    /**
     * Telegram bot.
     */
    private final Bot bot;

    /**
     * Threads.
     */
    private final Threads threads;

    /**
     * Users.
     */
    private final Users users;

    /**
     * Ctor.
     * @param bot Telegram bot
     * @param threads Threads
     * @param users Users
     */
    public UnreadThreadsSync(
        final Bot bot,
        final Threads threads,
        final Users users
    ) {
        this.bot = bot;
        this.threads = threads;
        this.users = users;
    }

    @Override
    public void sync() throws IOException {
        for (final Map.Entry<Long, List<Thread>> item
            : this.threads.unread().entrySet()) {
            final String tid = this.users.user(item.getKey()).tid();
            for (final Thread thread : item.getValue()) {
                try {
                    this.bot.send(
                        new SendMessage()
                            .setChatId(tid)
                            .enableMarkdown(true)
                            .setText(thread.subject().getString("title"))
                    );
                } catch (final IOException err) {
                    throw new IOException("Error while notifying user", err);
                }
            }
        }
    }
}
