/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.mybatisplus.core.toolkit;

/**
 * Copy to jodd.util
 * <p>
 * Pool of <code>String</code> constants to prevent repeating of
 * hard-coded <code>String</code> literals in the code.
 * Due to fact that these are <code>public static final</code>
 * they will be inlined by java compiler and
 * reference to this class will be dropped.
 * There is <b>no</b> performance gain of using this pool.
 * Read: https://java.sun.com/docs/books/jls/third_edition/html/lexical.html#3.10.5
 * <ul>
 * <li>Literal strings within the same class in the same package represent references to the same <code>String</code> object.</li>
 * <li>Literal strings within different classes in the same package represent references to the same <code>String</code> object.</li>
 * <li>Literal strings within different classes in different packages likewise represent references to the same <code>String</code> object.</li>
 * <li>Strings computed by constant expressions are computed at compile time and then treated as if they were literals.</li>
 * <li>Strings computed by concatenation at run time are newly created and therefore distinct.</li>
 * </ul>
 */
public interface StringPool {

    String AND = "and";
    String AT = "@";
    String ASTERISK = "*";
    String STAR = ASTERISK;
    String COMMA = ",";
    String DASH = "-";
    String DOT = ".";
    String EMPTY = "";
    String EQUALS = "=";
    String LEFT_BRACKET = "(";
    String LEFT_CHEV = "<";
    String NEWLINE = "\n";
    String NULL = "null";
    String PERCENT = "%";
    String QUESTION_MARK = "?";
    String QUOTE = "\"";
    String RIGHT_BRACE = "}";
    String RIGHT_BRACKET = ")";
    String RIGHT_CHEV = ">";
    String SEMICOLON = ";";
    String SPACE = " ";
    String LEFT_SQ_BRACKET = "[";
    String RIGHT_SQ_BRACKET = "]";
    String UNDERSCORE = "_";
    String ONE = "1";
    String DOLLAR_LEFT_BRACE = "${";
    String HASH_LEFT_BRACE = "#{";
}
