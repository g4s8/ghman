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

import com.g4s8.ghman.env.EnvironmentVariables;
import com.g4s8.ghman.user.Users;
import com.g4s8.teletakes.bot.Bot;
import com.g4s8.teletakes.bot.BotSimple;
import com.g4s8.teletakes.fk.FkCallbackQuery;
import com.g4s8.teletakes.fk.FkCommand;
import com.g4s8.teletakes.tk.TkFallback;
import com.g4s8.teletakes.tk.TkFork;
import java.io.IOException;
import java.util.regex.Pattern;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.BotOptions;
import org.telegram.telegrambots.generics.LongPollingBot;

/**
 * GhMan Bot.
 *
 * @since 1.0
 * @todo #11:30mins Patterns in callback queries chains are distributed in
 *  multiple classes: for example, pattern for close issue is given here and in
 *  TkCloseIssue. Find a way to avoid this by maybe having string pattern
 *  representation here and passing Matcher to telegram takes (TkCloseIssue) or
 *  by having one class implementing Fork and containing both the current code
 *  of TkCloseIssue and the creation of the FkCallbackQuery.
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class GhBot implements Bot, LongPollingBot {

    /**
     * Bot.
     */
    private final BotSimple bot;

    /**
     * Ctor.
     * @param users Users.
     * @param env Environment.
     */
    public GhBot(final Users users, final EnvironmentVariables env) {
        this.bot = new BotSimple(
            new TkMarkdown(
                new TkFallback(
                    new TkFork(
                        new FkCommand(
                            "/notifications",
                            new TkNotifications(users)
                        ),
                        new FkCallbackQuery(
                            Pattern.compile(
                                "click:notification#(?<tid>[A-Za-z0-9]+)"
                            ),
                            new TkThread(users)
                        ),
                        new FkCallbackQuery(
                            Pattern.compile(
                                //@checkstyle LineLengthCheck (1 line)
                                "click:notification.close\\?repo=(?<coords>.+/.+)&issue=(?<issue>\\d+)"
                            ),
                            new TkCloseIssue(users)
                        )
                    ),
                    new FbUnauthorized(env)
                )
            ),
            env.botUsername(),
            env.botToken()
        );
    }

    @Override
    public String toString() {
        return this.getBotUsername();
    }

    @Override
    public void onUpdateReceived(final Update update) {
        this.bot.onUpdateReceived(update);
    }

    @Override
    public String getBotUsername() {
        return this.bot.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return this.bot.getBotToken();
    }

    @Override
    public BotOptions getOptions() {
        return this.bot.getOptions();
    }

    @Override
    public void clearWebhook() throws TelegramApiRequestException {
        this.bot.clearWebhook();
    }

    @Override
    public void send(final SendMessage msg) throws IOException {
        this.bot.send(msg);
    }
}
