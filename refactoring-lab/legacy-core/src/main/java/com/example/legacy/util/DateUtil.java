package com.example.legacy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {
    private static final String ISO = "yyyy-MM-dd";
    private DateUtil() {}

    public static Date parseIsoDate(String s) {
        if (s == null) return null;
        SimpleDateFormat f = new SimpleDateFormat(ISO);
        try {
            return f.parse(s);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date: " + s, e);
        }
    }

    public static String formatIsoDate(Date d) {
        if (d == null) return "";
        SimpleDateFormat f = new SimpleDateFormat(ISO);
        return f.format(d);
    }
}
