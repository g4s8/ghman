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

import com.g4s8.ghman.env.EnvironmentVariables;
import com.g4s8.ghman.user.GhAuthException;
import com.g4s8.ghman.user.User;
import com.jcabi.github.mock.MkGithub;
import com.jcabi.matchers.XhtmlMatchers;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.cactoos.text.FormattedText;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Test for {@link FbUnauthorized}.
 * @since 1.0
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class FbUnauthorizedTest {

    @Test
    void returnsLinkForAuth() throws Exception {
        final String host = "ghman.com";
        new Assertion<>(
            "Returns link to login",
            new FbUnauthorized(
                new EnvironmentVariables(new MapOf<>(new MapEntry<>("APP_HOST", host)))
            ).handle(new Update(), new GhAuthException(new User.Fake(new MkGithub()), ""))
                .orElseThrow(
                    () -> new IllegalStateException("Response is not supposed to be empty!")
                ).xml(),
            XhtmlMatchers.hasXPath(
                // @checkstyle LineLengthCheck (1 line)
                new FormattedText("response/message/text[.='You need to [login with Github](http://%s/auth?ps=0)']", host).asString()
            )
        ).affirm();
    }

    @Test
    void ignoresNonGhAuthException() {
        new Assertion<>(
            "Ignores non GhAuthException exceptions",
            new FbUnauthorized(new EnvironmentVariables()).handle(new Update(), new Throwable())
                .isPresent(),
            new IsEqual<>(false)
        ).affirm();
    }

}
