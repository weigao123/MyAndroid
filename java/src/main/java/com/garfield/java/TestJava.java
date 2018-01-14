package com.garfield.java;

import com.garfield.java.datastructure.sort.OnSort;
import com.garfield.java.util.L;

import java.util.Arrays;

public class TestJava {

    public static void main(String[] args) {
        new TestJava().doTest();
    }

    private void doTest() {

        int[] a = new int[]{3, 5, 3, 1, 3, 2, 3, 2};
        new OnSort().sort(a);

        L.d(Arrays.toString(a));


    }


}
