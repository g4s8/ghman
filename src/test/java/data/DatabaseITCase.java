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
package data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.postgresql.jdbc2.optional.SimpleDataSource;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;
import ru.yandex.qatools.embed.postgresql.distribution.Version;

/**
 * Database abstract case.
 * @since 1.0
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class DatabaseITCase {

    /**
     * Data source.
     */
    private static DataSource src;

    /**
     * Embedded postgres.
     */
    private static final EmbeddedPostgres POSTGRES =
        new EmbeddedPostgres(Version.V11_1);

    /**
     * Data source.
     * @return Data source
     */
    protected final DataSource dataSource() {
        return DatabaseITCase.src;
    }

    @BeforeAll
    static void setupClass() throws IOException {
        final SimpleDataSource dsrc = new SimpleDataSource();
        dsrc.setUrl(DatabaseITCase.POSTGRES.start());
        DatabaseITCase.src = dsrc;
        final Flyway flyway = new Flyway();
        flyway.setDataSource(DatabaseITCase.src);
        flyway.migrate();
    }

    @AfterAll
    static void tearDownClass() throws SQLException {
        try (Connection con = DatabaseITCase.src.getConnection()) {
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
        if (DatabaseITCase.src != null) {
            DatabaseITCase.POSTGRES.stop();
        }
    }
}
