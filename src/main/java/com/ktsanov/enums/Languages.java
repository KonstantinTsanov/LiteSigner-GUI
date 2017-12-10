/* 
 * The MIT License
 *
 * Copyright 2017 Konstantin Tsanov <k.tsanov@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.ktsanov.enums;

import java.util.Locale;
import lombok.Getter;

/**
 * Any new language must be added here.
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public enum Languages {
    English("English", "en", "US"),
    Bulgarian("Български", "bg", "BG");
    @Getter
    private final String name;
    @Getter
    private final String shortLanguage;
    @Getter
    private final String shortCountry;

    /**
     * Constructor
     *
     * @param name - language
     * @param shortLanguage - short for language
     * @param shortCountry - short for country
     */
    private Languages(String name, String shortLanguage, String shortCountry) {
        this.name = name;
        this.shortLanguage = shortLanguage;
        this.shortCountry = shortCountry;
    }

    /**
     * Compose a locale off enum and return it
     *
     * @return new locale
     */
    public Locale getLocale() {
        return new Locale(getShortLanguage(), getShortCountry());
    }
}
