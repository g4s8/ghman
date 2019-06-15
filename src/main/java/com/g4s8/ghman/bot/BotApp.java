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

import com.g4s8.teletakes.bot.BotSimple;
import com.g4s8.teletakes.fk.FkCallbackQuery;
import com.g4s8.teletakes.fk.FkCommand;
import com.g4s8.teletakes.tk.TkFallback;
import com.g4s8.teletakes.tk.TkFork;
import com.jcabi.aspects.Tv;
import com.jcabi.log.Logger;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.BotSession;

/**
 * Telegram bot entry point.
 *
 * @since 1.0
 * @todo #2:30mins Fix the ClassDataAbstractionCouplingCheck & PMD rule
 *  exclusions in BotApp class
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.UnnecessaryFullyQualifiedName")
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
     * @param data Data source
     */
    public BotApp(final String name,
        final String token, final DataSource data) {
        this.name = name;
        this.token = token;
        this.data = data;
        this.api = new TelegramBotsApi();
    }

    @Override
    public void run() {
        try {
            final BotSimple bot = new BotSimple(
                new TkMarkdown(
                    new TkFallback(
                        new TkFork(
                            new FkCommand(
                                "/notifications",
                                    new TkNotifications(this.data)
                            ),
                            new FkCallbackQuery(
                                Pattern.compile("click:notification#(?<tid>[A-Za-z0-9]+)"),
                                    new TkThread(this.data)
                            )
                        ),
                        new FbUnauthorized()
                    )
                ),
                this.name, this.token
            );
            final BotSession session = this.api.registerBot(bot);
            Logger.info(this, "Bot %s has been started", this.name);
            while (!java.lang.Thread.currentThread().isInterrupted()) {
                try {
                    java.lang.Thread.sleep(Tv.TEN);
                } catch (final InterruptedException ignore) {
                    java.lang.Thread.currentThread().interrupt();
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
