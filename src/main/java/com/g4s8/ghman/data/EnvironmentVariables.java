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
package com.g4s8.ghman.data;

/**
 * Environment Variables Retriever.
 *
 * @since 1.0
 * @checkstyle NonStaticMethodCheck (100 lines)
 */
public final class EnvironmentVariables {

    /**
     * Retrieve GitHub Client Id.
     * @return Github Client Id
     */
    public String getGithubClientId() {
        return System.getenv("GH_CLIENT");
    }

    /**
     * Retrieve GitHub Client Secret.
     * @return Github secret
     */
    public String getGithubClientSecret() {
        return System.getenv("GH_SECRET");
    }

    /**
     * Retrieve Application Host.
     * @return Application host
     */
    public String getApplicationHost() {
        return System.getenv("APP_HOST");
    }
}
