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

import com.g4s8.ghman.user.Users;
import com.g4s8.teletakes.rs.RsText;
import com.g4s8.teletakes.rs.TmResponse;
import com.g4s8.teletakes.tk.TmTake;
import com.jcabi.github.Coordinates;
import com.jcabi.github.Issue;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Telegram take to close issue.
 * @since 1.0
 * @todo #11:30mins 1) find a way to avoid using static method Integer.parseInt()
 *  2) there too much method chaining in Issue.Smart() instance creation. It's
 *  hard to read and not very good in term of OO practice: we depend here on
 *  many objects and we want to promote loose coupling usually. Find a way to
 *  avoid it.
 */
public final class TkCloseIssue implements TmTake {

    /**
     * Callback query pattern.
     */
    private static final Pattern PTN_QUERY =
        Pattern.compile("click:notification.close\\?repo=(?<coords>.+/.+)&issue=(?<issue>\\d+)");

    /**
     * Users.
     */
    private final Users users;

    /**
     * Ctor.
     * @param users Users
     */
    public TkCloseIssue(final Users users) {
        this.users = users;
    }

    @Override
    public TmResponse act(final Update upd) throws IOException {
        final Matcher matcher =
            PTN_QUERY.matcher(upd.getCallbackQuery().getData());
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                new UncheckedText(
                    new FormattedText(
                        "Illegal request: %s", upd.getCallbackQuery().getData()
                    )
                ).asString()
            );
        }
        new Issue.Smart(
            this.users.user(
                upd.getCallbackQuery().getMessage().getChat()
            )
            .github().github().repos().get(
                new Coordinates.Simple(matcher.group("coords"))
            )
            .issues().get(Integer.parseInt(matcher.group("issue")))
        ).close();
        return new RsText("Issue closed");
    }
}
