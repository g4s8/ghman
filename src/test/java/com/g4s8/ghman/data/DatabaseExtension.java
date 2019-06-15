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
import java.util.Arrays;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.postgresql.jdbc2.optional.SimpleDataSource;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;
import ru.yandex.qatools.embed.postgresql.distribution.Version;

/**
 * Database abstract case.
 * @since 1.0
 * @todo #7:30min Find a way to get rid of static field and method of this
 *  class. Note that this class is JUnit 5 Extension and is used to prepare
 *  database to integration tests. See
 *  https://junit.org/junit5/docs/current/user-guide/#extensions-registration-declarative
 */
final class DatabaseExtension implements BeforeAllCallback, AfterAllCallback {

    /**
     * Data source.
     */
    private static DataSource src;

    /**
     * Embedded postgres.
     */
    private final EmbeddedPostgres postgres =
        new EmbeddedPostgres(Version.V11_1);

    @Override
    public void beforeAll(final ExtensionContext context) throws Exception {
        final SimpleDataSource dsrc = new SimpleDataSource();
        dsrc.setUrl(this.postgres.start());
        DatabaseExtension.src = dsrc;
        final Flyway flyway = new Flyway();
        flyway.setDataSource(DatabaseExtension.src);
        flyway.migrate();
    }

    @Override
    public void afterAll(final ExtensionContext context) throws Exception {
        try (Connection con = DatabaseExtension.src.getConnection()) {
            for (final String sql : Arrays.asList(
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
        if (DatabaseExtension.src != null) {
            this.postgres.stop();
        }
    }

    /**
     * Data source.
     * @return Data source
     */
    static DataSource dataSource() {
        return DatabaseExtension.src;
    }
}