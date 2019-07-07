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
import com.g4s8.ghman.env.EnvironmentVariables;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import org.takes.facets.auth.PsByFlag;
import org.takes.facets.auth.PsChain;
import org.takes.facets.auth.PsCookie;
import org.takes.facets.auth.TkAuth;
import org.takes.facets.auth.codecs.CcAes;
import org.takes.facets.auth.codecs.CcCompact;
import org.takes.facets.auth.codecs.CcHex;
import org.takes.facets.fork.FkParams;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.tk.TkWrap;

/**
 * Takes app entry point.
 * @since 1.0
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class TkApp extends TkWrap {

    /**
     * Ctor.
     * @param data Data source
     * @param env Environment
     */
    TkApp(final DataSource data, final EnvironmentVariables env) {
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
                                new TkGitHubAuthorization(
                                    new PgUsers(data), env
                                )
                            ),
                            new FkGitHubAuthRedirection()
                        )
                    )
                ),
                new PsChain(
                    new PsByFlag(
                        "ps",
                        new PsByFlag.Pair(
                            Pattern.compile("\\d+"), new PsUserById()
                        )
                    ),
                    new PsCookie(
                        new CcHex(
                            new CcAes(
                                new CcCompact(), "temporarykey1234"
                            )
                        )
                    )
                )
            )
        );
    }
}
