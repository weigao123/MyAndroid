package com.garfield.java;

import com.garfield.java.datastructure.handle.HandleArray;
import com.garfield.java.datastructure.handle.HandleLinked;
import com.garfield.java.datastructure.handle.HandleTree;
import com.garfield.java.datastructure.sort.MergeSort;
import com.garfield.java.datastructure.util.ArrayUtils;
import com.garfield.java.testclass.A;
import com.garfield.java.util.L;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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
