package com.garfield.java.util;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;

/**
 * Created by gaowei on 2017/6/30.
 */

public class L {

    public static void d(Object obj) {
        String s;
        if (obj instanceof int[]) {
            s = Arrays.toString((int[])obj);
        } else if (obj instanceof char[]) {
            s = Arrays.toString((char[])obj);
        } else {
            s = obj.toString();
        }
        System.out.print(s);
    }

    public static void dl(Object obj) {
        String s;
        if (obj instanceof int[]) {
            s = Arrays.toString((int[])obj);
        } else if (obj instanceof char[]) {
            s = Arrays.toString((char[])obj);
        } else {
            s = obj.toString();
        }
        System.out.println(s);
    }

    public static void ds(Object obj) {
        String s;
        if (obj instanceof int[]) {
            s = Arrays.toString((int[])obj);
        } else if (obj instanceof char[]) {
            s = Arrays.toString((char[])obj);
        } else {
            s = obj.toString();
        }
        System.out.print(s + " ");
    }

    public static void println() {
        System.out.println();
    }

    public static void c(char c) {
        System.out.print(c);
        System.out.print(" ");
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void cwait(Condition condition) {
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void owait(Object object) {
        try {
            object.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
