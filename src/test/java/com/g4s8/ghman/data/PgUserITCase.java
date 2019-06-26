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

import com.g4s8.ghman.user.GhAuthException;
import com.g4s8.ghman.user.User;
import com.g4s8.hamcrest.json.JsonHas;
import com.g4s8.hamcrest.json.JsonValueIs;
import com.jcabi.github.RtGithub;
import java.io.IOException;
import org.cactoos.iterable.IterableOf;
import org.hamcrest.core.AllOf;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.Throws;
import org.mockito.Mockito;
import org.telegram.telegrambots.api.objects.Chat;

/**
 * Test for {@link com.g4s8.ghman.data.PgUser}.
 * @since 1.0
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class PgUserITCase {

    /**
     * Database.
     * @checkstyle VisibilityModifierCheck (5 lines)
     */
    @RegisterExtension
    final DatabaseExtension source = new DatabaseExtension();

    @Test
    void throwsExceptionIfUserIsNotFound() {
        new Assertion<>(
            "Should throw exception while selecting not existing user",
            () -> new PgUser(this.source.database(), -1).github(),
            new Throws<>(
                "Failed to select token",
                IOException.class
            )
        ).affirm();
    }

    @Test
    void throwsExceptionIfUserTelegramDataNotFound() {
        new Assertion<>(
            "Should throw exception while selecting tname for not existing user",
            () -> new PgUser(this.source.database(), -1).telegram(),
            new Throws<>(
                "Failed to select telegram data",
                IOException.class
            )
        ).affirm();
    }

    @Test
    void throwsExceptionIfUserTelegramIdNotFound() {
        new Assertion<>(
            "Should throw exception while selecting tid for not existing user",
            () -> new PgUser(this.source.database(), -1).tid(),
            new Throws<>(
                "Failed to select telegram id",
                IOException.class
            )
        ).affirm();
    }

    @Test
    void returnsUserId() {
        final long uid = 45;
        new Assertion<>(
            "Should return user id",
            new PgUser(this.source.database(), uid).uid(),
            new IsEqual<>(uid)
        ).affirm();
    }

    @Test
    void throwsExceptionIfTokenIsEmpty() throws IOException {
        final User user = new PgUsers(this.source.database())
            .user(mockChat(458L, "test1"));
        user.authorize("");
        new Assertion<>(
            "Should throw exception if token is empty",
            user::github,
            new Throws<>(
                "Github token was not found",
                GhAuthException.class
            )
        ).affirm();
    }

    @Test
    void returnsGhUser() throws IOException {
        final User user = new PgUsers(this.source.database())
            .user(mockChat(408L, "test2"));
        final String token = "some_token";
        user.authorize(token);
        new Assertion<>(
            "Should return github API instance with token",
            user.github().github(),
            new IsEqual<>(new RtGithub(token))
        ).affirm();
    }

    @Test
    void returnsTelegramId() throws IOException {
        final long tid = 409L;
        final User user = new PgUsers(this.source.database())
            .user(mockChat(tid, "test3"));
        new Assertion<>(
            "Should return telegram id",
            user.tid(),
            new IsEqual<>(String.valueOf(tid))
        ).affirm();
    }

    @Test
    void returnsTelegram() throws IOException {
        final long tid = 401L;
        final String uname = "test4";
        final User user = new PgUsers(this.source.database())
            .user(mockChat(tid, uname));
        new Assertion<>(
            "Should return json with telegram name and id",
            user.telegram(),
            new AllOf<>(
                new IterableOf<>(
                    new JsonHas("firstName", new JsonValueIs(uname)),
                    new JsonHas("uid", new JsonValueIs(tid))
                )
            )
        ).affirm();
    }

    /**
     * Configures {@link Chat} mock for tests.
     * @param tid Telegtam id
     * @param umane User name
     * @return Mocked {@link Chat} instance
     */
    private static Chat mockChat(final long tid, final String umane) {
        final Chat chat = Mockito.mock(Chat.class);
        Mockito.when(chat.getId()).thenReturn(tid);
        Mockito.when(chat.getFirstName()).thenReturn(umane);
        return chat;
    }
}
