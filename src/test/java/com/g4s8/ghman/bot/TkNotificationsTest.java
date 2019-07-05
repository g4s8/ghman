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
import com.jcabi.matchers.XhtmlMatchers;
import javax.json.Json;
import org.cactoos.text.FormattedText;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.mockito.Mockito;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Test for {@link TkNotifications}.
 * @since 1.0
 */
class TkNotificationsTest {

    @Test
    void returnsInlineKeyboard() throws Exception {
        final Update upd = Mockito.mock(Update.class);
        Mockito.when(upd.getMessage()).thenReturn(Mockito.mock(Message.class));
        final String title = "title";
        final String id = "id";
        final String subject = "subject";
        final String subjone = "Typo";
        final String subjtwo = "Improvement";
        final String idone = "469";
        final String idtwo = "12";
        // @checkstyle LineLengthCheck (1 line)
        final String btn = "response/message/keyboard/inline/row/button[.='%s'][@callback='click:notification#%s']";
        new Assertion<>(
            "Returns unread notifications count and buttons for each notification",
            new TkNotifications(
                new Users.Fake(
                    new RequestGithub(
                        Json.createArrayBuilder()
                            .add(
                                Json.createObjectBuilder().add(id, idone)
                                .add(subject, Json.createObjectBuilder().add(title, subjone))
                            )
                            .add(
                                Json.createObjectBuilder().add(id, idtwo)
                                .add(subject, Json.createObjectBuilder().add(title, subjtwo))
                            )
                            .build().toString()
                    )
                )
            ).act(upd).xml(),
            XhtmlMatchers.hasXPaths(
                "response/message/text[.='You have 2 unread notifications:']",
                new FormattedText(btn, subjone, idone).asString(),
                new FormattedText(btn, subjtwo, idtwo).asString()
            )
        ).affirm();
    }

}
