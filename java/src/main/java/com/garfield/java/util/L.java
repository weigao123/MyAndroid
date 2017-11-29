package com.garfield.java.util;

import java.util.concurrent.locks.Condition;

/**
 * Created by gaowei on 2017/6/30.
 */

public class L {

    public static void d(Object s) {
        System.out.println(s);
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
