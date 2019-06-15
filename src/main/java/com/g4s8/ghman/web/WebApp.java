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
package com.g4s8.ghman.web;

import com.g4s8.ghman.App;
import com.jcabi.log.Logger;
import java.io.IOException;
import javax.sql.DataSource;
import org.takes.http.FtCli;
import org.takes.tk.TkGreedy;
import org.takes.tk.TkGzip;
import org.takes.tk.TkMeasured;
import org.takes.tk.TkVersioned;

/**
 * Web application entry point.
 *
 * @since 1.0
 */
public final class WebApp implements Runnable {

    /**
     * Command line options.
     */
    private final App.Options opts;

    /**
     * Data source.
     */
    private final DataSource data;

    /**
     * Ctor.
     * @param opts Options
     * @param data Data source
     */
    public WebApp(final App.Options opts, final DataSource data) {
        this.opts = opts;
        this.data = data;
    }

    @Override
    public void run() {
        try {
            new FtCli(
                new TkMeasured(
                    new TkVersioned(
                        new TkGreedy(
                            new TkGzip(
                                new TkApp(this.data)
                            )
                        )
                    )
                ), this.opts
            ).start(
                () -> Thread.currentThread().isInterrupted()
            );
        } catch (final IOException err) {
            Logger.error(
                this,
                "Web failed: %[exception]s", err
            );
        }
    }
}
