package com.sk.skala.shopapi.tools;

public class StringUtil {

    private StringUtil() {
    }

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean hasText(String value) {
        return !isBlank(value);
    }
}
