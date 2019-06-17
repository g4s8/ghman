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

import com.g4s8.ghman.user.GhAuthException;
import com.g4s8.teletakes.fb.TmFallback;
import com.g4s8.teletakes.rs.RsText;
import com.g4s8.teletakes.rs.TmResponse;
import java.util.Optional;
import org.cactoos.text.FormattedText;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Telegram bot Unauthorized.
 *
 * @since 1.0
 * @todo #2:30min Implement Unit tests for FbUnauthorized class
 *  use JUNIT and cactoos-matchers wrapper.
 */
public final class FbUnauthorized implements TmFallback {

    @Override
    public Optional<TmResponse> handle(final Update update, final Throwable throwable) {
        final Optional<TmResponse> rsp;
        if (throwable instanceof GhAuthException) {
            rsp = Optional.of(
                new RsText(
                    new FormattedText(
                        "You need to [login with Github](http://%s/auth?ps=%d",
                        System.getenv("APP_HOST"),
                        GhAuthException.class.cast(throwable).user().uid()
                    )
                )
            );
        } else {
            rsp = Optional.empty();
        }
        return rsp;
    }
}
