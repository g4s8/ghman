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

import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.mockito.Mockito;
import org.telegram.telegrambots.api.objects.Chat;

/**
 * Test for {@link TmUser.FromChat}.
 * @since 1.0
 */
final class TmUserFromChatTest {

    @Test
    void returnsId() {
        final long tid = 456;
        new Assertion<>(
            "Returns tlg user id",
            new TmUser.FromChat(this.mock(tid, "Max", null, null)).tid(),
            new IsEqual<>(tid)
        ).affirm();
    }

    @Test
    void returnsFirstName() {
        final String fname = "Julia";
        new Assertion<>(
            "Returns tlg user first name",
            new TmUser.FromChat(this.mock(0, fname, null, null)).firstName(),
            new IsEqual<>(fname)
        ).affirm();
    }

    @Test
    void returnsLastName() {
        final String lname = "Ortega";
        new Assertion<>(
            "Returns tlg user last name",
            new TmUser.FromChat(this.mock(0, "girl", lname, null)).lastName().get(),
            new IsEqual<>(lname)
        ).affirm();
    }

    @Test
    void returnsUserName() {
        final String uname = "SuperMan";
        new Assertion<>(
            "Returns tlg user name",
            new TmUser.FromChat(this.mock(0, "boy", null, uname)).userName().get(),
            new IsEqual<>(uname)
        ).affirm();
    }

    @Test
    void returnsEmptyLastName() {
        new Assertion<>(
            "Returns empty opt if tlg user last name is absent",
            new TmUser.FromChat(this.mock(0, "man", null, null)).lastName().has(),
            new IsEqual<>(false)
        ).affirm();
    }

    @Test
    void returnsEmptyUserName() {
        new Assertion<>(
            "Returns empty opt if tlg user name is absent",
            new TmUser.FromChat(this.mock(0, "woman", null, null)).lastName().has(),
            new IsEqual<>(false)
        ).affirm();
    }

    /**
     * Mocks {@link Chat} instance.
     * @param tid Telegram id
     * @param name First name
     * @param lname Last name
     * @param username User name
     * @return Chat
     * @checkstyle ParameterNumberCheck (500 lines)
     */
    private Chat mock(final long tid, final String name, final String lname,
        final String username) {
        final Chat res = Mockito.mock(Chat.class);
        Mockito.when(res.getId()).thenReturn(tid);
        Mockito.when(res.getUserName()).thenReturn(username);
        Mockito.when(res.getLastName()).thenReturn(lname);
        Mockito.when(res.getFirstName()).thenReturn(name);
        return res;
    }
}
