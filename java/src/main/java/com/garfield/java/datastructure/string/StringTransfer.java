package com.garfield.java.datastructure.string;

import java.util.Stack;

/**
 * Created by gaowei on 17/7/24.
 */

public class StringTransfer {

    public static String toLower(String s) {
        if (s == null) {
            return null;
        }
        String result = "";
        for (int i = 0; i < s.length(); i ++) {
            char c = s.charAt(i);
            if (c > 'A' && c < 'Z') {
                // 必须要有char强制转换，否则是个整数非字符
                result += (char) (c - ('A' - 'a'));
            }
        }
        return result;
    }

    public static int toInt(String s) {
        int result = 0;
        if (s == null || s.length() == 0) {
            throw new IllegalArgumentException("要转换的字符串为空，无法转换！");
        }
        boolean isNegative = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (i == 0 && c == '-') {
                isNegative = true;
            } else if (c >= '0' && c <= '9') {
                // 10 * result，向前移一位
                result = 10 * result + (c - '0');
            }
        }
        return isNegative ? -result : result;
    }

    /**
     * 利用栈反转字符串
     */
    public static String revert(String s) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            stack.push(c);
        }
        String result = "";
        while (!stack.empty()) {
            result += stack.pop();
        }
        return result;
    }

    public static int revertAndToInt(String s) {
        if (s == null) {
            return 0;
        }
        int result = 0;
        boolean isNegative = false;
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            stack.push(c);
            if (i == s.length() - 1 && c == '-') {
                isNegative = true;
            }
        }
        while (!stack.empty()) {
            char c = stack.pop();
            if (c >= '0' && c <= '9') {
                // 10 * result，向前移一位
                result = 10 * result + (c - '0');
            }
        }
        return isNegative ? -result : result;
    }
}
