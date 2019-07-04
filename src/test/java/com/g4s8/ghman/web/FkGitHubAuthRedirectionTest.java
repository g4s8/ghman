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

import com.g4s8.ghman.env.EnvironmentVariables;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import org.cactoos.list.ListOf;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.cactoos.text.FormattedText;
import org.hamcrest.core.AllOf;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.takes.rq.RqFake;
import org.takes.rs.RsPrint;

/**
 * Test for {@link FkGitHubAuthRedirection}.
 * @since 1.0
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
class FkGitHubAuthRedirectionTest {

    @Test
    void redirects() throws Exception {
        final String client = "123";
        final String host = "ghman.com";
        new Assertion<>(
            "Redirects to github with params",
            new RsPrint(
                new FkGitHubAuthRedirection(
                    new EnvironmentVariables(
                        new MapOf<>(
                            new MapEntry<>("GH_CLIENT", client),
                            new MapEntry<>("APP_HOST", host)
                        )
                    )
                ).route(new RqFake()).get()
            ).print(),
            new AllOf<>(
                new ListOf<>(
                    new StringContains("https://github.com/login/oauth/authorize"),
                    new StringContains(
                        new FormattedText(
                            "redirect_uri=%s",
                            URLEncoder.encode(
                                new FormattedText("https://%s/auth", host).asString(),
                                Charset.forName("UTF-8").name()
                            )
                        ).asString()
                    ),
                    new StringContains(new FormattedText("client_id=%s", client).asString()),
                    new StringContains("scope=notifications")
                )
            )
        ).affirm();
    }

}
