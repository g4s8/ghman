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

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.sql.DataSource;
import org.cactoos.list.ListOf;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.postgresql.ds.PGSimpleDataSource;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;
import ru.yandex.qatools.embed.postgresql.distribution.Version;

/**
 * JUnit 5 extension to set up database. This extension is meant to be used in
 * all database related integration tests.
 * @since 1.0
 */
public final class DatabaseExtension implements BeforeEachCallback, AfterEachCallback {

    /**
     * Data source.
     */
    private final PGSimpleDataSource src = new PGSimpleDataSource();

    /**
     * Embedded postgres.
     */
    private final EmbeddedPostgres postgres = new EmbeddedPostgres(Version.V11_1);

    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        this.src.setUrl(this.postgres.start());
        Flyway.configure().dataSource(this.src).load().migrate();
    }

    @Override
    public void afterEach(final ExtensionContext context) throws Exception {
        try (Connection con = this.src.getConnection()) {
            for (final String sql : new ListOf<>(
                "DROP SCHEMA public CASCADE",
                "CREATE SCHEMA public",
                "GRANT ALL ON SCHEMA public TO postgres",
                "GRANT ALL ON SCHEMA public TO public"
            )) {
                try (PreparedStatement stm = con.prepareStatement(sql)) {
                    stm.executeUpdate();
                }
            }
        }
        this.postgres.stop();
    }

    /**
     * Data source.
     * @return Data source
     */
    public DataSource database() {
        return this.src;
    }
}
