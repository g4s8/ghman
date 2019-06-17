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
package com.g4s8.ghman.env;

import java.util.Map;
import org.cactoos.text.FormattedText;

/**
 * Environment Variables Retriever.
 *
 * @since 1.0
 */
public final class EnvironmentVariables {

    /**
     * Github Client Id Key String.
     */
    private static final String GH_CLIENT = "GH_CLIENT";

    /**
     * Github Secret Key String.
     */
    private static final String GH_SECRET = "GH_SECRET";

    /**
     * Application Host Key String.
     */
    private static final String APP_HOST = "APP_HOST";

    /**
     * Environment Variables Map.
     */
    private final Map<String, String> envvars;

    /**
    * Ctor.
    */
    public EnvironmentVariables() {
        this(System.getenv());
    }

    /**
    * Ctor.
    * @param envvars Preloaded Environment Variables Map
    */
    public EnvironmentVariables(final Map<String, String> envvars) {
        this.envvars = envvars;
    }

    /**
     * Retrieve GitHub Client Id.
     * @return Github Client Id
     * @throws IllegalStateException if Github Client Id is missing
     *  from system environment variables.
     */
    public String githubClientId() throws IllegalStateException {
        return this.envvars.computeIfAbsent(
            this.GH_CLIENT,
            this::missingKeyExceptionThrower
        );
    }

    /**
     * Retrieve GitHub Client Secret.
     * @return Github secret
     * @throws IllegalStateException if Github Secret is missing from
     *  system environment variables.
     */
    public String githubClientSecret() throws IllegalStateException {
        return this.envvars.computeIfAbsent(
            this.GH_SECRET,
            this::missingKeyExceptionThrower
        );
    }

    /**
     * Retrieve Application Host.
     * @return Application host
     * @throws IllegalStateException if Application host is missing from
     *  system environment variables.
     */
    public String applicationHost() throws IllegalStateException {
        return this.envvars.computeIfAbsent(
            this.APP_HOST,
            this::missingKeyExceptionThrower
        );
    }

    /**
     * Throws IllegalStateException with message containing the missing key.
     * @param key The key which is missing from the environment variables
     * @return Value to be added. Always null for this function.
     * @throws IllegalStateException Always thrown.
     */
    private String missingKeyExceptionThrower(final String key) throws IllegalStateException {
        throw new IllegalStateException(
            new FormattedText(
                "%s is missing from System Environment variables",
                key
            ).toString()
        );
    }
}
