package com.garfield.java;

import com.garfield.java.javacreate.JAssist;


public class TestJava {

    public static void main(String[] args) {
        new TestJava().doTest();
    }

    private void doTest() {
        //new TestMulti().doTest();



        try {
            new JAssist().test();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
