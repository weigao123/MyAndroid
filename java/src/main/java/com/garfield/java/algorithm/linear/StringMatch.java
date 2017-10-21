package com.garfield.java.algorithm.linear;

/**
 * Created by gaowei on 2017/10/19.
 */

public class StringMatch {

    public static int simple(char[] source, char[] target) {
        int i = 0, j = 0;
        while (i < source.length) {
            if (source[i] == target[j]) {
                ++i;
                ++j;
                if (j == target.length) {    //匹配越界即成功，可以放到while中
                    return i - j;            //退回匹配的起始
                }
            } else {
                i = i - j + 1;    //i回溯到上轮起始的下一个
                j = 0;            //j重置
            }
        }
        return -1;
    }

    public static int simple2(char[] source, char[] target) {
        int i = 0, j = 0;
        while (i < source.length && j < target.length) {
            if (source[i] == target[j]) {
                ++i;
                ++j;
            } else {
                i = i - j + 1;
                j = 0;
            }
        }
        if (j == target.length) {
            return i - j;
        }
        return -1;
    }

    public static int kmp(char[] source, char[] target) {
        int i = 0, j = 0;
        while (i < source.length && j < target.length) {
            if (source[i] == target[j]) {
                ++i;
                ++j;
            } else {
                i = i - j + 1;
                j = 0;
            }
        }
        if (j >= target.length) {
            return i - j;
        }
        return -1;
    }

    public static int[] next(char[] p) {
        int[] next = new int[p.length];
        next[0] = -1;
        int j = 0;
        while (j < p.length) {
            int n = j - 1;
            while ()
        }



        return next;
    }




    public static int[] getNext(char[] p) {
//        int[] next = new int[target.length];
//        int i = 0, j = 0;
//        next[0] = 0;
//        while (i < target.length - 1) {
//            if (j == 0 || target[i] == target[j]) {
//                ++i;
//                ++j;
//                next[i] = j;
//            } else {
//                j = next[j];
//            }
//        }
        int pLen = p.length;
        int[] next = new int[pLen];
        int k = -1;
        int j = 0;
        next[0] = -1; // next数组中next[0]为-1
        while (j < pLen - 1) {
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
