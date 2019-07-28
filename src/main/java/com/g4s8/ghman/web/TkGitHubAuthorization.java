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
import com.g4s8.ghman.user.User;
import com.g4s8.ghman.user.Users;
import com.jcabi.http.Request;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.JsonResponse;
import java.io.IOException;
import org.cactoos.Func;
import org.cactoos.func.IoCheckedFunc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.auth.RqAuth;
import org.takes.rq.RqHref;
import org.takes.rs.RsText;

/**
 * Github Authorization Take.
 * @since 1.0
 */
final class TkGitHubAuthorization implements Take {

    /**
     * Code request param.
     */
    private static final String P_CODE = "code";

    /**
     * Data source.
     */
    private final Users users;

    /**
     * Authorization function.
     * @todo #43:30min Introduce a meaningful abstraction for this. The
     *  current implementation in the secondary constructor should reside in its
     *  own class and there should be a Fake implementation for use in the tests.
     *  Do not forget to correct unit test accordingly.
     */
    private final Func<org.takes.Request, String> auth;

    /**
     * Logger.
     */
    @SuppressWarnings("PMD.LoggerIsNotStaticFinal")
    private final Logger logger;

    /**
     * Ctor.
     * @param users Users
     * @param env Environment
     */
    TkGitHubAuthorization(final Users users, final EnvironmentVariables env) {
        this(
            users,
            req -> new JdkRequest("https://github.com/login/oauth/access_token")
                .method(Request.POST)
                .uri()
                .queryParam("client_id", env.githubClientId())
                .queryParam("client_secret", env.githubClientSecret())
                .queryParam(
                    TkGitHubAuthorization.P_CODE,
                    new RqHref.Smart(req).single(TkGitHubAuthorization.P_CODE)
                ).back()
                .header("Accept", "application/json")
                .fetch()
                .as(JsonResponse.class)
                .json()
                .readObject()
                .getString("access_token")
        );
    }

    /**
     * Ctor.
     * @param users Data source
     * @param auth Authorization
     */
    TkGitHubAuthorization(final Users users, final Func<org.takes.Request, String> auth) {
        this(users,  auth, LoggerFactory.getLogger(TkGitHubAuthorization.class));
    }

    /**
     * Ctor.
     * @param users Data source
     * @param auth Authorization
     * @param logger Logger
     */
    TkGitHubAuthorization(
        final Users users,
        final Func<org.takes.Request, String> auth,
        final Logger logger
    ) {
        this.users = users;
        this.auth = auth;
        this.logger = logger;
    }

    @Override
    public Response act(final org.takes.Request req) throws IOException {
        final String token = new IoCheckedFunc<>(this.auth).apply(req);
        final String urn = new RqAuth(req).identity().urn();
        final User user = this.users.user(Long.parseLong(urn.split(":")[2]));
        user.authorize(token);
        this.logger.info(
            "User {} ({}) authorized by token",
            user.uid(), urn
        );
        return new RsText("Authorized successfully!");
    }
}
