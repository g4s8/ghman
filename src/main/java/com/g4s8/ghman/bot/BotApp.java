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
 *
 *
 */
package com.g4s8.ghman.bot;

import com.g4s8.ghman.user.GhAuthException;
import com.g4s8.ghman.user.GhThread;
import com.g4s8.ghman.user.User;
import com.g4s8.ghman.user.Users;
import com.g4s8.teletakes.bot.BotSimple;
import com.g4s8.teletakes.fk.FkCallbackQuery;
import com.g4s8.teletakes.fk.FkCommand;
import com.g4s8.teletakes.rs.RsInlineKeyboard;
import com.g4s8.teletakes.rs.RsText;
import com.g4s8.teletakes.rs.TmResponse;
import com.g4s8.teletakes.tk.TkFallback;
import com.g4s8.teletakes.tk.TkFork;
import com.jcabi.aspects.Tv;
import com.jcabi.github.Comment;
import com.jcabi.github.Issue;
import com.jcabi.log.Logger;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import org.cactoos.list.ListOf;
import org.cactoos.list.Mapped;
import org.cactoos.map.MapEntry;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Joined;
import org.cactoos.text.TextOf;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.BotSession;

/**
 * Telegram bot entry point.
 *
 * @since 1.0
 * @todo #1:30min Refactor this class:
 *  move actual logic to separate takes and keep only
 *  composition structure in this class.
 */
public final class BotApp implements Runnable {

    static {
        ApiContextInitializer.init();
    }

    /**
     * Bot name.
     */
    private final String name;

    /**
     * Bot token.
     */
    private final String token;

    /**
     * Bot API.
     */
    private final TelegramBotsApi api;

    /**
     * Data source.
     */
    private final DataSource data;

    /**
     * Ctor.
     * @param name Bot name
     * @param token Bot token
     * @param data
     */
    public BotApp(final String name, final String token, final DataSource data) {
        this.name = name;
        this.token = token;
        this.data = data;
        this.api = new TelegramBotsApi();
    }

    @Override
    public void run() {
        try {
            final BotSession session = this.api.registerBot(
                new BotSimple(
                    new TkMarkdown(
                        new TkFallback(
                            new TkFork(
                                new FkCommand(
                                    "/notifications",
                                    upd -> {
                                        final User user = new Users(this.data).user(upd.getMessage().getChat());
                                        final ListOf<GhThread> notifications = new ListOf<>(user.github().notifications());
                                        return new RsInlineKeyboard(
                                            new RsText(new FormattedText("You have %d unread notifications:", notifications.size())),
                                            new Mapped<>(
                                                ntf -> Collections.singleton(
                                                    new MapEntry<>(
                                                        ntf.title(),
                                                        String.format("click:notification#%s", ntf.nid())
                                                    )
                                                ),
                                                notifications
                                            )
                                        );
                                    }
                                ),
                                new FkCallbackQuery(
                                    Pattern.compile("click:notification#(?<tid>[A-Za-z0-9]+)"),
                                    upd -> {
                                        final User user = new Users(this.data).user(upd.getCallbackQuery().getMessage().getChat());
                                        final GhThread thread = user.github().thread(upd.getCallbackQuery().getData().split("#")[1]);
                                        final Issue issue = thread.resolve();
                                        return new RsText(
                                            new FormattedText(
                                                "[#%d](%s) - %s\n\n%s",
                                                issue.number(),
                                                String.format("https://github.com/%s/issues/%d", issue.repo().coordinates(), issue.number()),
                                                new Issue.Smart(issue).title(),
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
                                                            issue.comments().iterate(new Date(thread.lastRead().toEpochMilli()))
                                                        )
                                                    )
                                                )
                                            )
                                        );
                                    }
                                )
                            ),
                            (upd, err) -> {
                                final Optional<TmResponse> rsp;
                                if (err instanceof GhAuthException) {
                                    rsp = Optional.of(
                                        new RsText(
                                            new FormattedText(
                                                "You need to [login with Github](http://%s/auth?ps=%d",
                                                System.getenv("APP_HOST"),
                                                GhAuthException.class.cast(err).user().uid()
                                            )
                                        )
                                    );
                                } else {
                                    rsp = Optional.empty();
                                }
                                return rsp;
                            }
                        )
                    ),
                    this.name, this.token
                )
            );
            Logger.info(this, "Bot %s has been started", this.name);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(Tv.TEN);
                } catch (final InterruptedException ignore) {
                    Thread.currentThread().interrupt();
                }
            }
            session.stop();
        } catch (final TelegramApiRequestException err) {
            Logger.error(
                this,
                "Telegram registration failed: %[exception]s",
                err
            );
        }
    }
}
