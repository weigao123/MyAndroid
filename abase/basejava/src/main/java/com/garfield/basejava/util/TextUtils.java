package com.garfield.basejava.util;

public class TextUtils {
    public static boolean equalsOneOf(String source, String... elements) {
        for (String str : elements) {
            if (str != null && str.equals(source)) return true;
        }
        return false;
    }

    public static boolean notEqualsOneOf(String source, String... elements) {
        for (String str : elements) {
            if (str != null && str.equals(source)) return false;
        }
        return true;
    }
}
