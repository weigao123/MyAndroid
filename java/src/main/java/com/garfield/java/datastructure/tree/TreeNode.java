package com.garfield.java.datastructure.tree;

/**
 * Created by gaowei on 2017/5/22.
 */

public class TreeNode {

    public char value;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(char key) {
        this(key, null, null);
    }

    public TreeNode(char value, TreeNode left, TreeNode right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static TreeNode generateTree() {
        /**
         *        A
         *      B      C
         *    D      E    F
         *  G  H      I
         *
         *  前序：ABDGHCEIF
         *  中序：GDHBAEICF
         *  后序：GHDBIEFCA
         */
        TreeNode g = new TreeNode('G');
        TreeNode h = new TreeNode('H');
        TreeNode d = new TreeNode('D', g, h);
        TreeNode b = new TreeNode('B', d, null);
        TreeNode i = new TreeNode('I');
        TreeNode e = new TreeNode('E', null, i);
        TreeNode f = new TreeNode('F');
        TreeNode c = new TreeNode('C', e, f);
        TreeNode a = new TreeNode('A', b, c);
        return a;
    }

    public static TreeNode generateSubTree() {
        TreeNode g = new TreeNode('G');
        TreeNode h = new TreeNode('H');
        TreeNode d = new TreeNode('d', g, h);
        return d;
    }

    private static TreeNode init2() {
        /**
         *  有道图
         *  前序：
         *  中序：
         *  后序：
         */
        TreeNode l = new TreeNode('L');
        TreeNode j = new TreeNode('J');
        TreeNode k = new TreeNode('K', null, l);
        TreeNode i = new TreeNode('I', j, k);
        TreeNode g = new TreeNode('G', i, null);
        TreeNode h = new TreeNode('H');
        TreeNode f = new TreeNode('F', g, h);
        TreeNode d = new TreeNode('D', null, f);
        TreeNode e = new TreeNode('E');
        TreeNode c = new TreeNode('C', e, null);
        TreeNode b = new TreeNode('B', c, d);
        TreeNode a = new TreeNode('A', b, null);
        return a;
    }
}
