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

package com.g4s8.ghman;

import com.g4s8.ghman.bot.BotApp;
import com.g4s8.ghman.data.FlywayDataSource;
import com.g4s8.ghman.data.SimpleDataSource;
import com.g4s8.ghman.web.WebApp;
import com.jcabi.log.VerboseRunnable;
import javax.sql.DataSource;
import org.cactoos.iterable.IterableEnvelope;
import org.cactoos.iterable.IterableOf;

/**
 * App entry point.
 *
 * @since 1.0
 */
public final class App {

    /**
     * Entry point.
     * @param args Args
     */
    public static void main(final String[] args) throws Exception {
        final Options opts = new Options(new IterableOf<>(args));
        final DataSource data = new FlywayDataSource(
            new SimpleDataSource(
                System.getenv()
            )
        ).value();
        final Thread web = new Thread(new WebApp(opts, data));
        web.checkAccess();
        web.setDaemon(false);
        web.setName(App.class.getSimpleName());
        web.setPriority(Thread.MAX_PRIORITY);
        final Thread bot = new Thread(
            new VerboseRunnable(
                new BotApp(opts.bot(), opts.token(), data)
            )
        );
        bot.checkAccess();
        bot.setDaemon(false);
        bot.setPriority(Thread.MAX_PRIORITY - 1);
        bot.setName(BotApp.class.getSimpleName());
        web.start();
        bot.start();
        while (web.isAlive() && bot.isAlive()) {
            try {
                Thread.sleep(0L);
            } catch (final InterruptedException err) {
                if (!web.isInterrupted()) {
                    web.interrupt();
                }
                if (!bot.isInterrupted()) {
                    bot.interrupt();
                }
            }
        }
        if (web.isAlive() && !web.isInterrupted()) {
            web.interrupt();
        }
        if (bot.isAlive() && !bot.isInterrupted()) {
            bot.interrupt();
        }
        web.join();
    }

    /**
     * Command line options.
     */
    public final static class Options extends IterableEnvelope<String> {

        /**
         * Ctor.
         * @param args Command line args
         */
        Options(final Iterable<String> args) {
            super(() -> args);
        }

        String token() {
            return "722534289:AAH0LYMRngVCdsTTmo69wAwqwdKxEetRUgk";
        }

        String bot() {
            return "gh2tm_bot";
        }
    }
}
