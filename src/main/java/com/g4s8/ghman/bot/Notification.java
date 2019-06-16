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
import com.g4s8.ghman.user.Users;
import com.g4s8.teletakes.bot.BotSimple;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Notify user into telegram chat.
 * @since 1.0
 */
public interface Notification {

    /**
     * Sends notification.
     * @throws IOException When fails
     */
    void send() throws IOException;

    /**
     * Unread threads notification.
     * @since 1.0
     * @todo #9:30 Continue implement notifications for user when new messages
     *  (unread github notification) arrive. To do that we need to monitor
     *  unread notifications by constantly checking `threads` table for unread
     *  threads. Schedule java thread (or any other synchronization process) to
     *  periodically call `PgThreads#unread()` to retrieve unread messages and
     *  use `Notification.UnreadThreads()` to send them.
     * @todo #9:30min Now this class send only github subject title as
     *  notification. Please aks ARC what exactly we want to send to user and
     *  create a class to extract and format this information from `JsonObject`
     *  (github subject) accordingly.
     * @todo #9:30min Test for `Notification.UnreadThreads` should be
     *  implemented after https://github.com/g4s8/teletakes/issues/14 is
     *  resolved. Please, while implementing try to avoid using mocked objects,
     *  use fake implementations instead.
     */
    final class UnreadThreads implements Notification {

        /**
         * Telegram bot.
         */
        private final BotSimple bot;

        /**
         * Users and notification map.
         */
        private final Map<Long, List<Thread>> ntfc;

        /**
         * Users.
         */
        private final Users users;

        /**
         * Ctor.
         * @param bot Telegram bot
         * @param ntfc Notifications list
         * @param users Users
         */
        public UnreadThreads(final BotSimple bot,
            final Map<Long, List<Thread>> ntfc, final Users users) {
            this.bot = bot;
            this.ntfc = ntfc;
            this.users = users;
        }

        @Override
        public void send() throws IOException {
            for (final Map.Entry<Long, List<Thread>> item
                : this.ntfc.entrySet()) {
                final String tid = this.users.user(item.getKey()).tid();
                for (final Thread thread : item.getValue()) {
                    try {
                        this.bot.sendMessage(
                            new SendMessage()
                                .setChatId(tid)
                                .enableMarkdown(true)
                                .setText(thread.subject().getString("title"))
                        );
                    } catch (final TelegramApiException err) {
                        throw new IOException("Error while notifying user", err);
                    }
                }
            }
        }
    }
}
