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

import com.jcabi.aspects.Tv;
import com.jcabi.log.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.BotSession;
import org.telegram.telegrambots.generics.LongPollingBot;

/**
 * Telegram bot entry point.
 *
 * @since 1.0
 * @todo #11:30mins Patterns in callback queries chains are distributed in
 *  multiple classes: for example, pattern for close issue is given here and in
 *  TkCloseIssue. Find a way to avoid this by maybe having string pattern
 *  representation here and passing Matcher to telegram takes (TkCloseIssue) or
 *  by having one class implementing Fork and containing both the current code
 *  of TkCloseIssue and the creation of the FkCallbackQuery.
 */
public final class BotApp implements Runnable {

    static {
        ApiContextInitializer.init();
    }

    /**
     * Bot.
     */
    private final LongPollingBot bot;

    /**
     * Bot API.
     */
    private final TelegramBotsApi api;

    /**
     * Ctor.
     * @param bot Bot
     */
    public BotApp(final LongPollingBot bot) {
        this.bot = bot;
        this.api = new TelegramBotsApi();
    }

    @Override
    public void run() {
        try {
            final BotSession session = this.api.registerBot(this.bot);
            Logger.info(this, "Bot %s has been started", this.bot);
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
