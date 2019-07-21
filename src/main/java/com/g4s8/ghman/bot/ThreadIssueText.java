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

import com.jcabi.github.Comment;
import com.jcabi.github.Issue;
import java.io.IOException;
import java.util.Date;
import org.cactoos.list.Mapped;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Joined;
import org.cactoos.text.TextEnvelope;
import org.cactoos.text.TextOf;

/**
 * Telegram bot notifications text with latest comments.
 *
 * @since 1.0
 * @todo #35:30min Implement Unit tests for this class
 *  use junit, cactoos-matchers and fake class (no mocks).
 */
public final class ThreadIssueText extends TextEnvelope {

    /**
     * Ctor.
     * @param issue Issue
     * @param since Last time comments were seen in milliseconds
     * @throws IOException If some field can't be read from issue
     */
    public ThreadIssueText(final Issue.Smart issue, final long since) throws IOException {
        this(issue, new Date(since));
    }

    /**
     * Ctor.
     * @param issue Issue
     * @param since Last time comments were seen
     * @throws IOException If some field can't be read from issue
     */
    public ThreadIssueText(final Issue.Smart issue, final Date since) throws IOException {
        super(
            new FormattedText(
                "[#%d](%s) - %s\n\n%s",
                issue.number(),
                new FormattedText(
                    "https://github.com/%s/issues/%d",
                    issue.repo().coordinates(),
                    issue.number()
                ),
                issue.title(),
                new Joined(
                    new TextOf("\n\n"),
                    new Mapped<>(
                        cmt -> new FormattedText(
                            "[@%s](https://github.com/%s) > %s",
                            cmt.author().login(),
                            cmt.author().login(),
                            cmt.body()
                        ),
                        new Mapped<>(
                            Comment.Smart::new,
                            issue.comments().iterate(since)
                        )
                    )
                )
            )
        );
    }
}
