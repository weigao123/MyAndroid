package com.garfield.java;

import com.garfield.java.algorithm.linear.StringMatch;
import com.garfield.java.util.L;

public class TestJava {

    public static void main(String[] args) {
        new TestJava().doTest();
    }

    private void doTest() {

        int r = StringMatch.simple2("abcdef".toCharArray(), "def".toCharArray());
        L.p(r);

        int[] re = StringMatch.getNext("aaaa".toCharArray());

    }


}
