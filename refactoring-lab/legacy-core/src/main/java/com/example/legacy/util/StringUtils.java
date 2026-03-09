package com.example.legacy.util;

public final class StringUtils {
    private StringUtils() {}
    public static boolean isBlank(String s) { return s == null || s.trim().length() == 0; }
    public static String safeTrim(String s) { return s == null ? null : s.trim(); }
}
