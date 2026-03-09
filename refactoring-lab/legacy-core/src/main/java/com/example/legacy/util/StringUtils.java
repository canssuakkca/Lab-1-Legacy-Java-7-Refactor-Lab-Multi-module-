package com.example.legacy.util;

public final class StringUtils {
    private StringUtils() {}

    public static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public static String safeTrim(String s) {
        return s == null ? null : s.strip();
    }
}