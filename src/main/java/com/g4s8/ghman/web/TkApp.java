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

import com.g4s8.ghman.data.PgUsers;
import com.g4s8.ghman.user.User;
import com.g4s8.ghman.utility.EnvironmentVariables;
import com.jcabi.http.Request;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.JsonResponse;
import com.jcabi.log.Logger;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import org.apache.http.client.utils.URIBuilder;
import org.cactoos.scalar.IoChecked;
import org.takes.Response;
import org.takes.facets.auth.Identity;
import org.takes.facets.auth.Pass;
import org.takes.facets.auth.PsByFlag;
import org.takes.facets.auth.PsChain;
import org.takes.facets.auth.PsCookie;
import org.takes.facets.auth.RqAuth;
import org.takes.facets.auth.TkAuth;
import org.takes.facets.auth.codecs.CcAes;
import org.takes.facets.auth.codecs.CcCompact;
import org.takes.facets.auth.codecs.CcHex;
import org.takes.facets.fork.FkFixed;
import org.takes.facets.fork.FkParams;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.misc.Opt;
import org.takes.rq.RqHref;
import org.takes.rs.RsRedirect;
import org.takes.rs.RsText;
import org.takes.tk.TkWrap;

/**
 * Takes app entry point.
 * @since 1.0
 * @todo #1:30min Refactor this class:
 *  extract additional classes for takes and pass, move
 *  actual logic to these classes, keep only composition structure
 *  in TkApp.
 * @checkstyle LineLengthCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle ClassFanOutComplexityCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
final class TkApp extends TkWrap {

    /**
     * Ctor.
     * @param data Data source
     * @param environment EnvironmentVariables
     */
    TkApp(final DataSource data, final EnvironmentVariables environment) {
        super(
            new TkAuth(
                new TkFork(
                    new FkRegex(
                        "/ping",
                        new TkSync(data)
                    ),
                    new FkRegex(
                        "/auth",
                        new TkFork(
                            new FkParams(
                                "code", ".+",
                                req -> {
                                    final String code = new RqHref.Smart(req)
                                        .single("code");
                                    final String token = new JdkRequest("https://github.com/login/oauth/access_token")
                                        .method(Request.POST)
                                        .uri()
                                        .queryParam("client_id", environment.getGithubClientId())
                                        .queryParam("client_secret", environment.getGithubClientSecret())
                                        .queryParam("code", code)
                                        .back()
                                        .header("Accept", "application/json")
                                        .fetch()
                                        .as(JsonResponse.class)
                                        .json()
                                        .readObject()
                                        .getString("access_token");
                                    final String urn = new RqAuth(req).identity().urn();
                                    final User user = new PgUsers(data)
                                        .user(Long.parseLong(urn.split(":")[2]));
                                    user.authorize(token);
                                    Logger.info(
                                        TkApp.class,
                                        "User %d (%s) authorized by token",
                                        user.uid(), urn
                                    );
                                    return new RsText("Authorized successfully!");
                                }
                            ),
                            new FkFixed(
                                req -> new RsRedirect(
                                    new IoChecked<>(
                                        () -> new URIBuilder("https://github.com/login/oauth/authorize")
                                            .addParameter("redirect_uri", String.format("https://%s/auth", environment.getApplicationHost()))
                                            .addParameter("client_id", environment.getGithubClientId())
                                            .addParameter("scope", "notifications")
                                            .build()
                                    ).value().toASCIIString()
                                )
                            )
                        )
                    )
                ),
                new PsChain(
                    new PsByFlag(
                        "ps",
                        new PsByFlag.Pair(
                            Pattern.compile("\\d+"),
                            new Pass() {
                                @Override
                                public Opt<Identity> enter(final org.takes.Request req) throws IOException {
                                    return new Opt.Single<>(
                                        new Identity.Simple(
                                            String.format(
                                                "urn:uid:%s",
                                                new RqHref.Smart(req).single("ps")
                                            )
                                        )
                                    );
                                }

                                @Override
                                public Response exit(final Response response, final Identity identity) {
                                    return response;
                                }
                            }
                        )
                    ),
                    new PsCookie(
                        new CcHex(
                            new CcAes(
                                new CcCompact(),
                                "temporarykey1234"
                            )
                        )
                    )
                )
            )
        );
    }
}
