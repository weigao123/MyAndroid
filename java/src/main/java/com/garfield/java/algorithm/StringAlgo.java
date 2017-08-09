package com.garfield.java.algorithm;

import java.util.Stack;

/**
 * Created by gaowei on 17/7/24.
 */

public class StringAlgo {

    public static int transfer(String str) {
        if (str == null) {
            return 0;
        }
        int sum = 0;
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            char s = str.charAt(i);
            stack.push(s);
        }
        boolean isFirstChar = true;
        boolean isNegative = false;
        while (!stack.empty()) {
            char value = stack.pop();
            if (isFirstChar && value == '-') {
                isNegative = true;
                isFirstChar = false;
                continue;
            } else if (value > '9' || value < '0') {
                continue;
            }
            sum *= 10;
            sum += value - '0';
        }
        return isNegative ? -sum : sum;
    }

    public static String revert(String str) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            char s = str.charAt(i);
            stack.push(s);   //将每一个字符压入栈中
        }
        String result = "";
        while (!stack.empty()) {
            result += stack.pop();   //按出栈顺序组成字符串，即为反转字符串
        }
        return result;
    }

    public static int atoi2(String s) {
        int retInt = 0;
        if (s == null || s.length() == 0) {
            throw new IllegalArgumentException("要转换的字符串为空，无法转换！");
        }
        boolean isNegative = false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0) {
                if (s.charAt(i) == '-') {
                    isNegative = true;
                    continue;
                }
            } else {
                if (s.charAt(i) > '9' || s.charAt(i) < '0') {
                    continue;
                }
            }
            retInt *= 10;
            retInt += s.charAt(i) - '0';
        }
        return isNegative ? -retInt : retInt;
    }
}