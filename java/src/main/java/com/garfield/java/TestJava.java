package com.garfield.java;

import com.garfield.java.datastructure.handle.HandleArray;
import com.garfield.java.datastructure.handle.HandleLinked;
import com.garfield.java.datastructure.handle.HandleTree;
import com.garfield.java.datastructure.sort.ShellSort;
import com.garfield.java.datastructure.util.ArrayUtils;

public class TestJava {

    public static void main(String[] args) {
        new TestJava().doTest();
    }

    private void doTest() {
        HandleLinked.test();
        HandleTree.test();
        HandleArray.test();


        new ShellSort().sort(ArrayUtils.getRandomArray(28));
        //TreePreOrder.preOrderStack2(TreeNode.treeNode);


        //ISort sort = new SelectSort();
        //int[]  a= ArrayUtils.getRandomArray(50);
        //sort.sort(a);



    }





}
