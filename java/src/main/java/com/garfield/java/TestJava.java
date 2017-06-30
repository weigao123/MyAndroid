package com.garfield.java;

import com.garfield.java.multithread.TestMulti;

public class TestJava {

    public static void main(String[] args) {
        new TestJava().doTest();
    }

    private void doTest() {
        new TestMulti().doTest();
    }
}
