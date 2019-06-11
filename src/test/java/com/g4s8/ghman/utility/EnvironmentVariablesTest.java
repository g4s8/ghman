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
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;

/**
 * Test case for {@link EnvironmentVariables}.
 *
 * @since 1.0
 */
public final class EnvironmentVariablesTest {

    /**
     * Github Client Id valid key value.
     */
    private static final String CLIENT_ID = "CLIENT";

    /**
     * Github Secret valid key value.
     */
    private static final String SECRET = "SECRET";

    /**
     * Application Host valid key value.
     */
    private static final String APP_HOST = "HOST";

    /**
     * Environment Variables Key/Value Map.
     */
    private final Map<String, String> validvarmap = new HashMap<>();

    @BeforeEach
    void preloadEnvironmentVariables() {
        this.validvarmap.put("GH_CLIENT", EnvironmentVariablesTest.CLIENT_ID);
        this.validvarmap.put("GH_SECRET", EnvironmentVariablesTest.SECRET);
        this.validvarmap.put("APP_HOST", EnvironmentVariablesTest.APP_HOST);
    }

    @Test
    void shouldReturnClientValue() {
        new Assertion<>(
            "Failed to get Github Client value from environment variables",
            new EnvironmentVariables(this.validvarmap).getGithubClientId(),
            new IsEqual<>(EnvironmentVariablesTest.CLIENT_ID)
        ).affirm();
    }

    @Test
    void shouldReturnSecretValue() {
        new Assertion<>(
            "Failed to get Github Secret value from environment variables",
            new EnvironmentVariables(this.validvarmap).getGithubClientSecret(),
            new IsEqual<>(EnvironmentVariablesTest.SECRET)
        ).affirm();
    }

    @Test
    void shouldReturnAppHostValue() {
        new Assertion<>(
            "Failed to get Application Host value from environment variables",
            new EnvironmentVariables(this.validvarmap).getApplicationHost(),
            new IsEqual<>(EnvironmentVariablesTest.APP_HOST)
        ).affirm();
    }
}
