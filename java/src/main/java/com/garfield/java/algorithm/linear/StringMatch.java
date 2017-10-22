package com.garfield.java.algorithm.linear;

/**
 * Created by gaowei on 2017/10/19.
 */

public class StringMatch {

    public static int bf(char[] s, char[] t) {
        int i = 0, j = 0;
        while (i < s.length && j < t.length) {    //在这里判断
            if (s[i] == t[j]) {
                ++i;
                ++j;
            } else {
                i = i - j + 1;
                j = 0;
            }
        }
        if (j == t.length) {
            return i - j;
        }
        return -1;
    }

    public static int bf2(char[] s, char[] t) {
        int i = 0, j = 0;
        while (i < s.length) {
            if (s[i] == t[j]) {
                ++i;
                ++j;
                if (j == t.length) {    //匹配越界即成功，可以放到while中
                    return i - j;            //退回匹配的起始
                }
            } else {
                i = i - j + 1;    //一旦不匹配，i回溯到上轮起始的下一个
                j = 0;            //j重置
            }
        }
        return -1;
    }

    public static int kmp(char[] s, char[] t) {
        int i = 0, j = 0;
        int[] next = kmpNext(t);
        while (i < s.length && j < t.length) {
            // j==-1时，只有头元素的next=-1，表示现在是t的头元素正在和s[i]比较且不同，朴素算法
            if (j == -1 || s[i] == t[j]) {
                ++i;
                ++j;
            } else {
                j = next[j];
            }
        }
        if (j == t.length) {
            return i - j;
        }
        return -1;
    }

    public static int[] kmpNext(char[] p) {
        int[] next = new int[p.length];
        int k = -1;   //迭代求next时，前k位和后k位相同
        int j = 0;    //p的index
        next[0] = -1;
        while (j < p.length - 1) {
            // k==-1，表示已经没有可以重复的字符了，next直接=0
            if (k == -1 || p[j] == p[k]) {
                k++;
                j++;
                next[j] = k;
            } else {
                k = next[k];
            }
        }
        return next;
    }

}
