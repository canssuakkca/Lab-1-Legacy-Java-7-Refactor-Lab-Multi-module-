package com.example.legacy.domain;

import java.util.Arrays;

public enum Department {
    AI("AI"),
    DATA("DATA"),
    PLATFORM("PLATFORM"),
    SECURITY("SECURITY"),
    UNKNOWN("UNKNOWN");

    private final String value;

    Department(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Department fromString(String s) {
        if (s == null || s.isBlank()) return UNKNOWN;
        return Arrays.stream(values())
                .filter(d -> d.value.equalsIgnoreCase(s.trim()))
                .findFirst()
                .orElse(UNKNOWN);
    }
}