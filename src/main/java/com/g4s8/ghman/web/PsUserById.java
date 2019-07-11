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
import org.cactoos.text.FormattedText;
import org.cactoos.text.TextOf;
import org.takes.Request;
import org.takes.Response;
import org.takes.facets.auth.Identity;
import org.takes.facets.auth.Pass;
import org.takes.misc.Opt;
import org.takes.rq.RqHref;

/**
 * User by identity extraction.
 * @since 1.0
 * @todo #4:30min Implement unit tests for PsUserById.
 *  Refer to takes framework Unit testing guide @ https://github.com/yegor256/takes#unit-testing
 */
final class PsUserById implements Pass {

    /**
     * Flag.
     */
    private final String flag;

    /**
     * Ctor.
     * @param flag Flag
     */
    PsUserById(final String flag) {
        this.flag = flag;
    }

    /**
     * Ctor.
     */
    PsUserById() {
        this("ps");
    }

    @Override
    public Opt<Identity> enter(final Request req) throws IOException {
        return new Opt.Single<>(
            new Identity.Simple(
                new FormattedText(
                    new TextOf("urn:uid:%s"), new RqHref.Smart(req).single(this.flag)
                ).toString()
            )
        );
    }

    @Override
    public Response exit(final Response response, final Identity identity) {
        return response;
    }
}
