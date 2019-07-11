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
import org.apache.http.client.utils.URIBuilder;
import org.cactoos.scalar.IoChecked;
import org.cactoos.text.FormattedText;
import org.cactoos.text.TextOf;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.fork.FkWrap;
import org.takes.facets.fork.Fork;
import org.takes.misc.Opt;
import org.takes.rs.RsRedirect;

/**
 * Github Authorization Redirection.
 * @since 1.0
 * @todo #4:30min Implement unit tests for FkGitHubAuthRedirection. Refer
 *  to takes framework Unit testing guide @ https://github.com/yegor256/takes#unit-testing
 */
public final class FkGitHubAuthRedirection extends FkWrap {

    /**
     * Ctor.
     */
    public FkGitHubAuthRedirection() {
        super(
            new Fork() {
                private Take take = req -> new RsRedirect(
                    new IoChecked<>(
                        () -> new URIBuilder("https://github.com/login/oauth/authorize")
                            .addParameter(
                                "redirect_uri",
                                new FormattedText(
                                    new TextOf("https://%s/auth"), System.getenv("APP_HOST")
                                ).toString()
                            )
                            .addParameter("client_id", System.getenv("GH_CLIENT"))
                            .addParameter("scope", "notifications")
                            .build()
                    ).value().toASCIIString()
                );
                @Override
                public Opt<Response> route(final Request req) throws IOException {
                    return new Opt.Single<>(this.take.act(req));
                }
            }
        );
    }
}
