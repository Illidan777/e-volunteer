package com.evolunteer.evm.common.utils.string;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringSymbolUtils {
    public static final String VENDOR_CODE_SPLITERATOR = "(?<=\\G...)";
    public static final String DOT_SPLITERATOR = "\\.";
    public static final String DOT = ".";
    public static final String COMMA = ",";
    public static final String ZERO = "0";
    public static final String EMPTY = "Empty";
    public static final String SPACE = " ";
    public static final String SEMICOLON = ";";
    public static final String EMPTY_STRING = "";
    public static final String LEFT_BRACKET = "(";
    public static final String RIGHT_BRACKET = ")";
    public static final String HYPHEN = "-";
    public static final String UNDERSCORE = "_";
    public static final String BACKSLASH = "\\\\";
    public static final String PLUS = "+";
    public static final String LINE_SEPARATOR = "\n";
    public static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    public static List<Character> getAlphabet() {
        final List<Character> alphabet = new ArrayList<>();
        for (char c: StringSymbolUtils.ALPHABET) {
            alphabet.add(Character.toUpperCase(c));
        }
        return alphabet;
    }
}
