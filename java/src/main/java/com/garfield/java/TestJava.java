package com.garfield.java;

import com.garfield.java.javacreate.JAssist;

import sun.reflect.CallerSensitive;

public class TestJava {

    public static void main(String[] args) {
        new TestJava().doTest();
    }

    @CallerSensitive
    private void doTest() {
        //new TestMulti().doTest();



        try {
            new JAssist().test();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
