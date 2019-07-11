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
package com.g4s8.ghman.data;

import com.jcabi.log.Logger;
import java.io.IOException;
import java.util.Map;
import javax.sql.DataSource;
import org.cactoos.Scalar;
import org.cactoos.scalar.IoChecked;
import org.cactoos.scalar.Solid;
import org.cactoos.text.FormattedText;
import org.cactoos.text.TextOf;

/**
 * Simple data source.
 *
 * @since 1.0
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class SimpleDataSource implements Scalar<DataSource> {

    /**
     * Origin.
     */
    private final IoChecked<DataSource> origin;

    /**
     * Ctor.
     * @param env Environment
     */
    @SuppressWarnings("PMD.ConstructorOnlyInitializesOrCallOtherConstructors")
    public SimpleDataSource(final Map<String, String> env) {
        SimpleDataSource.check(
            env,
            "DB_HOST", "DB_PORT", "DB_NAME", "DB_USER"
        );
        this.origin = new IoChecked<>(new Solid<>(() -> fromEnv(env)));
    }

    @Override
    public DataSource value() throws IOException {
        return this.origin.value();
    }

    /**
     * Data source from environment.
     * @param env Environment
     * @return Data source
     */
    private static DataSource fromEnv(final Map<String, String> env) {
        final org.postgresql.jdbc2.optional.SimpleDataSource source =
            new org.postgresql.jdbc2.optional.SimpleDataSource();
        source.setUrl(
            new FormattedText(
                new TextOf("jdbc:postgresql://%s:%s/%s"),
                env.get("DB_HOST"), env.get("DB_PORT"), env.get("DB_NAME")
            ).toString()
        );
        source.setUser(env.get("DB_USER"));
        if (env.containsKey("DB_PASSWORD")) {
            source.setPassword(env.get("DB_PASSWORD"));
        }
        Logger.info(
            SimpleDataSource.class,
            "dataSource: %s", source.getUrl()
        );
        return source;
    }

    /**
     * Check environment has required variables.
     * @param env Environemnt
     * @param req Required variables
     * @throws IllegalArgumentException If check fails
     */
    private static void check(
        final Map<String, String> env, final String... req
    ) throws IllegalArgumentException {
        for (final String var : req) {
            if (!env.containsKey(var)) {
                throw new IllegalArgumentException(
                    new FormattedText(
                        new TextOf("Environment variable '%s' is required"),
                        var
                    ).toString()
                );
            }
        }
    }
}
