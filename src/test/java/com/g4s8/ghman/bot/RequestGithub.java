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

import com.jcabi.github.Gists;
import com.jcabi.github.Github;
import com.jcabi.github.Gitignores;
import com.jcabi.github.Limits;
import com.jcabi.github.Markdown;
import com.jcabi.github.Organizations;
import com.jcabi.github.Repos;
import com.jcabi.github.Search;
import com.jcabi.http.Request;
import com.jcabi.http.request.FakeRequest;
import java.net.HttpURLConnection;
import javax.json.JsonObject;

/**
 * Implementation of {@link Github} that to returns given in ctor request body.
 * @since 1.0
 */
@SuppressWarnings("PMD.TooManyMethods")
final class RequestGithub implements Github {

    /**
     * Request body.
     */
    private final String body;

    /**
     * Ctor.
     * @param body Body for request
     */
    RequestGithub(final String body) {
        this.body = body;
    }

    @Override
    public Request entry() {
        return new FakeRequest()
            .withBody(this.body)
            .withStatus(HttpURLConnection.HTTP_OK);
    }

    @Override
    public Repos repos() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Gists gists() {
        throw new UnsupportedOperationException();
    }

    @Override
    public com.jcabi.github.Users users() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Organizations organizations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Markdown markdown() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Limits limits() {
        return null;
    }

    @Override
    public Search search() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Gitignores gitignores() {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonObject meta() {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonObject emojis() {
        throw new UnsupportedOperationException();
    }
}
