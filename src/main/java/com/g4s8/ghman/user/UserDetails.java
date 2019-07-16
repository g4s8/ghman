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

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.telegram.telegrambots.api.objects.Chat;

/**
 * Details about user.
 * @since 1.0
 */
public interface UserDetails {

    /**
     * User details.
     * @return User info in json format
     */
    JsonObject details();

    /**
     * {@link UserDetails} from telegram chat.
     * @since 1.0
     */
    final class FromChat implements UserDetails {

        /**
         * Telegram chat with a user.
         */
        private final Chat chat;

        /**
         * Ctor.
         * @param chat Telegram chat
         */
        public FromChat(final Chat chat) {
            this.chat = chat;
        }

        @Override
        public JsonObject details() {
            final JsonObjectBuilder details = Json.createObjectBuilder()
                .add("firstName", this.chat.getFirstName());
            if (this.chat.getLastName() != null) {
                details.add("lastName", this.chat.getLastName());
            }
            if (this.chat.getUserName() != null) {
                details.add("userName", this.chat.getUserName());
            }
            details.add("uid", this.chat.getId());
            return details.build();
        }
    }

}
