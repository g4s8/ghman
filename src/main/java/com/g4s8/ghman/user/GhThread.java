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
 *
 *
 */

package com.g4s8.ghman.user;

import com.jcabi.github.Coordinates;
import com.jcabi.github.Github;
import com.jcabi.github.Issue;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.json.JsonObject;
import org.cactoos.scalar.IoChecked;

/**
 * Github notification thread.
 * @since 1.0
 */
public final class GhThread {

    /**
     * Github.
     */
    private final Github ghb;

    /**
     * Json.
     */
    private final JsonObject jsn;

    /**
     * Ctor.
     * @param github Github
     * @param jsn Notification json
     */
    public GhThread(final Github github, final JsonObject jsn) {
        this.ghb = github;
        this.jsn = jsn;
    }

    /**
     * Notification title.
     * @return Notification title
     */
    public String title() {
        return this.jsn.getJsonObject("subject").getString("title");
    }

    /**
     * Thread id.
     * @return Id
     */
    public String nid() {
        return this.jsn.getString("id");
    }

    /**
     * Resolve thread issue.
     * @return Github issue
     * @throws IOException If fails
     */
    public Issue resolve() throws IOException {
        final JsonObject subj = this.jsn.getJsonObject("subject");
        final String type = subj.getString("type");
        if (!type.equals("Issue")) {
            throw new UnsupportedThreadException(String.format("Thread subject type is `%s` - not supported yet", type));
        }
        final Matcher matcher = Pattern.compile("/repos/(?<coords>.+/.+)/issues/(?<num>\\d+)").matcher(
            new IoChecked<>(() -> new URI(subj.getString("url")).getPath()).value()
        );
        if (!matcher.matches()) {
            throw new IOException("Failed to match thread link");
        }
        return this.ghb.repos()
            .get(new Coordinates.Simple(matcher.group("coords")))
            .issues().get(Integer.parseInt(matcher.group("num")));
    }

    /**
     * Get last read timestamp or epoch if never read.
     * @return Instant timestamp
     */
    public Instant lastRead() {
        return Instant.parse(
            this.jsn.getString("last_read_at", Instant.EPOCH.toString())
        );
    }
}
