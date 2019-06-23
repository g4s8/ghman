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

import com.g4s8.ghman.user.GhUser;
import com.g4s8.ghman.user.Thread;
import com.g4s8.ghman.user.Users;
import com.g4s8.teletakes.rs.RsInlineKeyboard;
import com.g4s8.teletakes.rs.RsText;
import com.g4s8.teletakes.rs.TmResponse;
import com.g4s8.teletakes.tk.TmTake;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.cactoos.list.Mapped;
import org.cactoos.list.Solid;
import org.cactoos.map.MapEntry;
import org.cactoos.text.FormattedText;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Telegram bot Notification Command.
 *
 * @since 1.0
 * @todo #2:30min Implement Unit tests for TkNotifications class. Refer
 *  to takes framework Unit testing guide @ https://github.com/yegor256/takes#unit-testing
 */
public final class TkNotifications implements TmTake {

    /**
     * Users.
     */
    private final Users users;

    /**
     * Ctor.
     * @param users Users
     */
    public TkNotifications(final Users users) {
        this.users = users;
    }

    @Override
    public TmResponse act(final Update update) throws IOException {
        final GhUser user = this.users.user(update.getMessage().getChat()).github();
        final List<Thread> nts = new Solid<>(user.notifications());
        return new RsInlineKeyboard(
            new RsText(new FormattedText("You have %d unread notifications:", nts.size())),
            new Mapped<>(
                ntf -> Collections.singleton(
                    new MapEntry<>(
                        ntf.subject().getString("title"),
                        new FormattedText("click:notification#%s", ntf.tid()).toString()
                    )
                ),
                nts
            )
        );
    }
}
