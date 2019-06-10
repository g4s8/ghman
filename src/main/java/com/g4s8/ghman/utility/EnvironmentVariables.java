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
package com.g4s8.ghman.utility;

import java.util.HashMap;
import java.util.Map;

/**
 * Environment Variables Retriever.
 *
 * @since 1.0
 * @todo #5: Implement unit tests for this class.
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
        this(createEnvMap());
    }

    /**
    * Ctor.
    * @param envvars Preloaded Environment Variables
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
    public String getGithubClientId() throws IllegalStateException {
        return this.getEnvVarialbe(this.GH_CLIENT);
    }

    /**
     * Retrieve GitHub Client Secret.
     * @return Github secret
     * @throws IllegalStateException if Github Secret is missing from
     *  system environment variables.
     */
    public String getGithubClientSecret() throws IllegalStateException {
        return this.getEnvVarialbe(this.GH_SECRET);
    }

    /**
     * Retrieve Application Host.
     * @return Application host
     * @throws IllegalStateException if Application host is missing from
     *  system environment variables.
     */
    public String getApplicationHost() throws IllegalStateException {
        return this.getEnvVarialbe(this.APP_HOST);
    }

    /**
     * Retrieve a value from environmentvars map.
     * @param key The key whose associated value is to be returned
     * @return The value to which the specified key is mapped
     * @throws IllegalStateException if this map contains no mapping for the
     *  key
     */
    private String getEnvVarialbe(final String key) throws IllegalStateException {
        final String value = this.envvars.get(key);
        if (value == null) {
            throw new IllegalStateException();
        }
        return value;
    }

    /**
     * Create a Map of Pre configured System Environment variables.
     * @return The map initialized with the values from the system env.
     */
    private static Map<String, String> createEnvMap() {
        final Map<String, String> envmap = new HashMap<>();
        envmap.put(
            EnvironmentVariables.GH_CLIENT,
            System.getenv(EnvironmentVariables.GH_CLIENT)
        );
        envmap.put(
            EnvironmentVariables.GH_SECRET,
            System.getenv(EnvironmentVariables.GH_SECRET)
        );
        envmap.put(
            EnvironmentVariables.APP_HOST,
            System.getenv(EnvironmentVariables.APP_HOST)
        );
        return envmap;
    }
}
