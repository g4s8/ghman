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
package com.g4s8.ghman.user;

import com.jcabi.github.Comments;
import com.jcabi.github.Coordinates;
import com.jcabi.github.Event;
import com.jcabi.github.Github;
import com.jcabi.github.Issue;
import com.jcabi.github.IssueLabels;
import com.jcabi.github.Repo;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.json.JsonObject;
import org.cactoos.Scalar;
import org.cactoos.scalar.IoChecked;
import org.cactoos.scalar.SolidScalar;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.FormattedText;
import org.cactoos.text.TextOf;

/**
 * Issue for thread.
 * @since 1.0
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class ThreadIssue implements Issue {

    /**
     * Scalar.
     */
    private final Scalar<Issue> scalar;

    /**
     * Ctor.
     * @param ghb Github
     * @param thread Thread
     */
    public ThreadIssue(final Github ghb, final Thread thread) {
        this(
            () -> {
                final JsonObject subj = thread.subject();
                final String type = subj.getString("type");
                if (!type.equals("Issue")) {
                    throw new UnsupportedThreadException(
                        new FormattedText(
                            new TextOf("Thread subject type is `%s` - not supported yet"),
                            type
                        ).toString()
                    );
                }
                // @checkstyle LineLengthCheck (1 line)
                final Matcher matcher = Pattern.compile("/repos/(?<coords>.+/.+)/issues/(?<num>\\d+)").matcher(
                    new IoChecked<>(() -> new URI(subj.getString("url")).getPath()).value()
                );
                if (!matcher.matches()) {
                    throw new IOException("Failed to match thread link");
                }
                return ghb.repos()
                    .get(new Coordinates.Simple(matcher.group("coords")))
                    .issues().get(Integer.parseInt(matcher.group("num")));
            }
        );
    }

    /**
     * Primary ctor.
     * @param scalar Wrapped scalar
     */
    private ThreadIssue(final Scalar<Issue> scalar) {
        this.scalar = new SolidScalar<>(scalar);
    }

    @Override
    public Repo repo() {
        return new Unchecked<>(this.scalar).value().repo();
    }

    @Override
    public int number() {
        return new Unchecked<>(this.scalar).value().number();
    }

    @Override
    public Comments comments() {
        return new Unchecked<>(this.scalar).value().comments();
    }

    @Override
    public IssueLabels labels() {
        return new Unchecked<>(this.scalar).value().labels();
    }

    @Override
    public Iterable<Event> events() throws IOException {
        return new Unchecked<>(this.scalar).value().events();
    }

    @Override
    public boolean exists() throws IOException {
        return new Unchecked<>(this.scalar).value().exists();
    }

    @Override
    public void patch(final JsonObject json) throws IOException {
        new Unchecked<>(this.scalar).value().patch(json);
    }

    @Override
    public JsonObject json() throws IOException {
        return new Unchecked<>(this.scalar).value().json();
    }

    @Override
    public int compareTo(final Issue issue) {
        return new Unchecked<>(this.scalar).value().compareTo(issue);
    }

    @Override
    public boolean equals(final Object obj) {
        final boolean eql;
        if (obj instanceof ThreadIssue) {
            eql = Objects.equals(
                new Unchecked<>(this.scalar).value(),
                new Unchecked<>(ThreadIssue.class.cast(obj).scalar)
                    .value()
            );
        } else {
            eql = false;
        }
        return eql;
    }

    @Override
    public int hashCode() {
        return Objects.hash(new Unchecked<>(this.scalar).value());
    }
}
