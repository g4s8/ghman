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

import com.jcabi.github.Issue;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapEnvelope;
import org.cactoos.map.MapOf;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;

/**
 * Telegram buttons to act on an {@link Issue}.
 *
 * @since 1.0
 * @todo #35:30min Implement Unit tests for this class
 *  using junit, cactoos-matchers and fake class (no mocks).
 */
public final class ThreadIssueButtons extends MapEnvelope<String, String> {

    /**
     * Ctor.
     * @param issue Issue
     */
    public ThreadIssueButtons(final Issue issue) {
        this(new Issue.Smart(issue));
    }

    /**
     * Ctor.
     * @param issue Issue
     */
    public ThreadIssueButtons(final Issue.Smart issue) {
        super(() -> new MapOf<>(
            new MapEntry<>(
                "close",
                new UncheckedText(
                    new FormattedText(
                        "click:notification.close?repo=%s&issue=%d",
                        issue.repo().coordinates(), issue.number()
                    )
                ).asString()
            )
        ));
    }
}
