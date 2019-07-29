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

import com.g4s8.ghman.user.GhThread;
import com.g4s8.ghman.user.GhUser;
import com.g4s8.ghman.user.ThreadIssue;
import com.g4s8.ghman.user.Users;
import com.g4s8.teletakes.rs.RsInlineKeyboard;
import com.g4s8.teletakes.rs.RsText;
import com.g4s8.teletakes.rs.TmResponse;
import com.g4s8.teletakes.tk.TmTake;
import com.jcabi.github.Issue;
import java.io.IOException;
import org.cactoos.iterable.IterableOf;
import org.cactoos.scalar.IoChecked;
import org.cactoos.scalar.Ternary;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Telegram bot notifications button thread.
 *
 * @since 1.0
 * @todo #2:30min Implement Unit tests for TkThread class
 *  *  use JUNIT and cactoos-matchers wrapper.
 * @todo #35:30min Fix the ClassDataAbstractionCouplingCheck rule exclusion
 *  in this class. It is too complex, some more abstraction should be brought
 *  outside of it.
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class TkThread implements TmTake {

    /**
     * Users.
     */
    private final Users users;

    /**
     * Ctor.
     * @param users Users
     */
    public TkThread(final Users users) {
        this.users = users;
    }

    @Override
    public TmResponse act(final Update update) throws IOException {
        final GhUser user = this.users
            .user(update.getCallbackQuery().getMessage().getChat())
            .github();
        final GhThread thread = user.thread(
            update.getCallbackQuery().getData().split("#")[1]
        );
        final Issue.Smart issue = new Issue.Smart(
            new ThreadIssue(user.github(), thread)
        );
        final TmResponse txt = new RsText(
            new ThreadIssueText(issue, thread.lastRead().toEpochMilli())
        );
        return new IoChecked<>(
            new Ternary<>(
                issue.isOpen() && issue.author().equals(user.github().users().self()),
                new RsInlineKeyboard(
                    txt,
                    new IterableOf<>(new ThreadIssueButtons(issue).entrySet())
                ),
                txt
            )
        ).value();
    }
}
