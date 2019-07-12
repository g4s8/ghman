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

import java.io.IOException;
import java.net.URISyntaxException;
import javax.ws.rs.HttpMethod;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;
import org.cactoos.text.FormattedText;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.takes.Response;
import org.takes.facets.auth.Identity;
import org.takes.rq.RqFake;
import org.takes.rs.RsWithStatus;

/**
 * Test for {@link PsUserById}.
 * @since 1.0
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
class PsUserByIdTest {

    @Test
    void returnsIdentityOnEnter() throws IOException, URISyntaxException {
        final String uid = "123";
        final String param = "uid";
        new Assertion<>(
            "Creates identity from request",
            new PsUserById(param).enter(
                new RqFake(
                    HttpMethod.GET,
                    new URIBuilder("http://localhost").addParameter(param, uid).toString()
                )
            ).get().urn(),
            new IsEqual<>(new FormattedText("urn:uid:%s", uid).asString())
        ).affirm();
    }

    @Test
    void returnsResponseOnExit() {
        final Response resp = new RsWithStatus(HttpStatus.SC_OK);
        new Assertion<>(
            "On exit returns given response",
            new PsUserById().exit(resp, new Identity.Simple("urn:uid:0")),
            new IsEqual<>(resp)
        ).affirm();
    }
}
