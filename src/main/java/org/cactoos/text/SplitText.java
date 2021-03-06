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

package org.cactoos.text;

import org.cactoos.Text;
import org.cactoos.iterable.IterableEnvelope;

/**
 * Compat for {@link Split}.
 *
 * @since 1.0
 */
public final class SplitText extends IterableEnvelope<Text> {

    /**
     * Ctor.
     *
     * @param text The text
     * @param rgx The regex
     */
    public SplitText(final String text, final String rgx) {
        this(new Split(text, rgx));
    }

    /**
     * Ctor.
     * @param text The text
     * @param rgx The regex
     */
    public SplitText(final String text, final Text rgx) {
        this(new Split(text, rgx));
    }

    /**
     * Ctor.
     * @param text The text
     * @param rgx The regex
     */
    public SplitText(final Text text, final String rgx) {
        this(new Split(text, rgx));
    }

    /**
     * Ctor.
     * @param text The text
     * @param rgx The regex
     */
    public SplitText(final Text text, final Text rgx) {
        this(new Split(text, rgx));
    }

    /**
     * Ctor.
     * @param text The text
     * @param rgx The regex
     */
    public SplitText(final UncheckedText text, final UncheckedText rgx) {
        this(new Split(text, rgx));
    }

    /**
     * Ctor.
     * @param text Text
     */
    private SplitText(final Split text) {
        super(() -> text);
    }
}
