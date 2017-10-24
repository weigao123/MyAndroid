package com.garfield.java.datastructure.tree;

/**
 * Created by gaowei on 2017/5/22.
 */

public class TreeNode {

    public static TreeNode treeNode = init2();

    public char data;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(char key) {
        this(key, null, null);
    }

    public TreeNode(char data, TreeNode left, TreeNode right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    static void visit(TreeNode p) {
        System.out.print(p.data + " ");
    }

    private static TreeNode init() {
        /**
         *        A
         *      B    C
         *    D    E    F
         *  G  H    I
         *
         *  前序：ABDGHCEIF
         *  中序：GDHBAEICF
         *  后序：GHDBIEFCA
         */
        // 必须逆序建立，先建立子节点，再逆序往上建立
        TreeNode g = new TreeNode('G');
        TreeNode h = new TreeNode('H');
        TreeNode i = new TreeNode('I');
        TreeNode d = new TreeNode('D', g, h);
        TreeNode e = new TreeNode('E', null, i);
        TreeNode f = new TreeNode('F');
        TreeNode b = new TreeNode('B', d, null);
        TreeNode c = new TreeNode('C', e, f);
        TreeNode a = new TreeNode('A', b, c);
        return a;
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
