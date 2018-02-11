package com.garfield.java;

import com.garfield.java.datastructure.handle.HandleArray;
import com.garfield.java.datastructure.handle.HandleLinked;
import com.garfield.java.datastructure.handle.HandleTree;
import com.garfield.java.testclass.A;

public class TestJava extends A {

    public static void main(String[] args) {
        new TestJava().doTest();
    }

    private void doTest() {
        HandleLinked.test();
        HandleTree.test();
        HandleArray.test();


    }




}
