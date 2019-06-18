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
import com.g4s8.ghman.user.ThreadIssue;
import com.g4s8.ghman.user.Users;
import com.g4s8.teletakes.rs.RsText;
import com.g4s8.teletakes.rs.TmResponse;
import com.g4s8.teletakes.tk.TmTake;
import com.jcabi.github.Issue;
import java.io.IOException;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Telegram take to close issue.
 * @since 1.0
 */
public class TkCloseIssue implements TmTake {

    /**
     * Users.
     */
    private final Users users;

    /**
     * Ctor.
     * @param users Users
     */
    public TkCloseIssue(final Users users) {
        this.users = users;
    }

    @Override
    public TmResponse act(final Update upd) throws IOException {
        final GhUser user = this.users.user(
            upd.getCallbackQuery().getMessage().getChat()
        ).github();
        new Issue.Smart(
            new ThreadIssue(
                user.github(),
                user.thread(upd.getCallbackQuery().getData().split("#")[1])
            )
        ).close();
        return new RsText("Issue closed");
    }
}
