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
import com.jcabi.http.Request;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.JsonResponse;
import com.jcabi.log.Logger;
import java.io.IOException;
import javax.sql.DataSource;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.auth.RqAuth;
import org.takes.rq.RqHref;
import org.takes.rs.RsText;

/**
 * Github Authorization Take.
 * @since 1.0
 * @todo #4:30min replace those static loggers with something more OO
 *  (Related with
 *  https://www.yegor256.com/2019/03/19/logging-without-static-logger.html)
 * @todo #4:30min Implement unit tests for TkGitHubAuthorization. Refer
 *  to takes framework Unit testing guide @ https://github.com/yegor256/takes#unit-testing
 */
final class TkGitHubAuthorization implements Take {

    /**
     * Data source.
     */
    private final DataSource data;

    /**
     * Ctor.
     * @param data Data source
     */
    TkGitHubAuthorization(final DataSource data) {
        this.data = data;
    }

    @Override
    public Response act(final org.takes.Request req) throws IOException {
        final String codeparam = "code";
        final String code = new RqHref.Smart(req)
            .single(codeparam);
        final String token = new JdkRequest("https://github.com/login/oauth/access_token")
            .method(Request.POST)
            .uri()
            .queryParam("client_id", System.getenv("GH_CLIENT"))
            .queryParam("client_secret", System.getenv("GH_SECRET"))
            .queryParam(codeparam, code).back()
            .header("Accept", "application/json")
            .fetch()
            .as(JsonResponse.class)
            .json()
            .readObject()
            .getString("access_token");
        final String urn = new RqAuth(req).identity().urn();
        final User user = new PgUsers(this.data).user(Long.parseLong(urn.split(":")[2]));
        user.authorize(token);
        Logger.info(
            TkApp.class,
            "User %d (%s) authorized by token",
            user.uid(), urn
        );
        return new RsText("Authorized successfully!");
    }
}
