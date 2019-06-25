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

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.Throws;

/**
 * Test for {@link com.g4s8.ghman.data.PgUser}.
 * @since 1.0
 * @todo #7:30min Continue implement this integration test: add cases for all
 *  methods of {@link com.g4s8.ghman.data.PgUser} class. Do not forget about
 *  all possible exceptions.
 */
final class PgUserITCase {

    /**
     * Database.
     * @checkstyle VisibilityModifierCheck (5 lines)
     */
    @RegisterExtension
    DatabaseExtension source = new DatabaseExtension();

    @Test
    void throwsExceptionIfUserIsNotFound() {
        new Assertion<>(
            "Unexpected exception while selecting not existing user",
            () -> new PgUser(this.source.database(), 0).github(),
            new Throws<>(
                "Failed to select token",
                IOException.class
            )
        ).affirm();
    }

}
