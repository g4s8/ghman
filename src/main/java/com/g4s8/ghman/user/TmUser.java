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

import org.takes.misc.Opt;
import org.telegram.telegrambots.api.objects.Chat;

/**
 * Telegram user.
 * @since 1.0
 */
public interface TmUser {

    /**
     * Telegram user id.
     * @return Long id
     */
    Long tid();

    /**
     * Telegram user name.
     * @return String name
     */
    String firstName();

    /**
     * Telegram last name.
     * @return Last name
     */
    Opt<String> lastName();

    /**
     * Username from telegram.
     * @return Username
     */
    Opt<String> userName();

    /**
     * Telegram user info from chat.
     * @since 1.0
     */
    final class FromChat implements TmUser {

        /**
         * Telegram chat.
         */
        private final Chat chat;

        /**
         * Ctor.
         * @param chat Chat.
         */
        public FromChat(final Chat chat) {
            this.chat = chat;
        }

        @Override
        public Long tid() {
            return this.chat.getId();
        }

        @Override
        public String firstName() {
            return this.chat.getFirstName();
        }

        @Override
        public Opt<String> lastName() {
            final Opt<String> res;
            if (this.chat.getLastName() == null) {
                res = new Opt.Empty<>();
            } else {
                res = new Opt.Single<>(this.chat.getLastName());
            }
            return res;
        }

        @Override
        public Opt<String> userName() {
            final Opt<String> res;
            if (this.chat.getUserName() == null) {
                res = new Opt.Empty<>();
            } else {
                res = new Opt.Single<>(this.chat.getUserName());
            }
            return res;
        }
    }

}
