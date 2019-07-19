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

import com.g4s8.ghman.user.User;
import com.g4s8.ghman.user.Users;
import com.jcabi.github.mock.MkGithub;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import org.cactoos.iterable.IterableOf;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.takes.facets.auth.Identity;
import org.takes.facets.auth.RqWithAuth;
import org.takes.rq.RqFake;
import org.takes.rs.RsPrint;

/**
 * Test for {@link TkGitHubAuthorization}.
 * @since 1.0
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
class TkGitHubAuthorizationTest {

    @Test
    void authorizes() throws IOException {
        final AtomicInteger count = new AtomicInteger(0);
        new TkGitHubAuthorization(
            new Users.Fake(new IterableOf<>(new User.Fake(new MkGithub(), count))),
            req -> "123"
        ).act(new RqWithAuth(new Identity.Simple("urn:fake:1"), new RqFake()));
        new Assertion<>(
            "Authorizes user",
            count.get(),
            new IsEqual<>(1)
        ).affirm();
    }

    @Test
    void returnsMessage() throws IOException {
        new Assertion<>(
            "Returns user friendly message",
            new RsPrint(
                new TkGitHubAuthorization(
                    new Users.Fake(new IterableOf<>(new User.Fake(new MkGithub()))),
                    req -> "abc"
                ).act(new RqWithAuth(new Identity.Simple("urn:fake:5"), new RqFake()))
            ).printBody(),
            new StringContains("Authorized successfully!")
        ).affirm();
    }
}
