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

import com.g4s8.ghman.data.PgUsers;
import com.g4s8.ghman.user.GhThread;
import com.g4s8.ghman.user.GhUser;
import com.g4s8.ghman.user.ThreadIssue;
import com.g4s8.teletakes.rs.RsInlineKeyboard;
import com.g4s8.teletakes.rs.RsText;
import com.g4s8.teletakes.rs.TmResponse;
import com.g4s8.teletakes.tk.TmTake;
import com.jcabi.github.Comment;
import com.jcabi.github.Issue;
import java.io.IOException;
import java.util.Date;
import javax.sql.DataSource;
import org.cactoos.iterable.IterableOf;
import org.cactoos.list.Mapped;
import org.cactoos.map.MapEntry;
import org.cactoos.scalar.Ternary;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Joined;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Telegram bot notifications button thread.
 *
 * @since 1.0
 * @todo #2:30min Implement Unit tests for TkThread class
 *  *  use JUNIT and cactoos-matchers wrapper.
 * @todo #2:30min Fix the ClassDataAbstractionCouplingCheck rule exclusion
 *  in TkThread class. This class is too complex.
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class TkThread implements TmTake {

    /**
     * Data source.
     */
    private final DataSource data;

    /**
     * Ctor.
     * @param data Data source
     */
    public TkThread(final DataSource data) {
        this.data = data;
    }

    @Override
    public TmResponse act(final Update update) throws IOException {
        final GhUser user = new PgUsers(this.data)
            .user(update.getCallbackQuery().getMessage().getChat())
            .github();
        final GhThread thread = user.thread(
            update.getCallbackQuery().getData().split("#")[1]
        );
        final Issue issue = new ThreadIssue(user.github(), thread);
        final Issue.Smart smart = new Issue.Smart(issue);
        final TmResponse text = new RsText(
            new FormattedText(
                "[#%d](%s) - %s\n\n%s",
                issue.number(),
                new FormattedText(
                    "https://github.com/%s/issues/%d",
                    issue.repo().coordinates(),
                    issue.number()
                ).toString(),
                smart.title(),
                new Joined(
                    new TextOf("\n\n"),
                    new Mapped<>(
                        cmt -> new FormattedText(
                            "[@%s](https://github.com/%s) > %s",
                            cmt.author().login(),
                            cmt.author().login(),
                            cmt.body()
                        ),
                        new Mapped<>(
                            Comment.Smart::new,
                            issue.comments().iterate(
                                new Date(thread.lastRead().toEpochMilli())
                            )
                        )
                    )
                )
            )
        );
        return new Unchecked<>(
            new Ternary<>(
                () -> smart.isOpen()
                    && smart.author().equals(user.github().users().self()),
                new RsInlineKeyboard(
                    text,
                    new IterableOf<>(
                        new IterableOf<>(
                            new MapEntry<>(
                                "close",
                                new UncheckedText(
                                    new FormattedText(
                                        "click:notification.close?repo=%s&issue=%d",
                                        issue.repo().coordinates(), issue.number()
                                    )
                                ).asString()
                            )
                        )
                    )
                ),
                text
            )
        ).value();
    }
}
