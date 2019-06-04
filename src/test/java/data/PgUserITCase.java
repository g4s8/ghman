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

import com.g4s8.ghman.data.PgUser;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link com.g4s8.ghman.data.PgUser}.
 * @since 1.0
 * @todo #7:30min Continue implement this integtation test: add cases for all
 *  methods of {@link com.g4s8.ghman.data.PgUser} class. Do not forget about
 *  all possible exceptions.
 */
final class PgUserITCase extends DatabaseITCase {

    @Test
    void throwsExceptionIfUserIsNotFound() {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new PgUser(this.dataSource(), 0).github()
            ).getMessage(),
            new IsEqual<>("Failed to select token")
        );
    }

}
