package com.garfield.java.datastructure.tree;

import java.util.Stack;

import static com.garfield.java.datastructure.tree.TreeNode.visit;

/**
 * Created by gaowei on 2017/10/24.
 */

public class TreeInOrder {

    public static void inOrderRec(TreeNode p) {
        if (p != null) {
            inOrderRec(p.left);
            visit(p);
            inOrderRec(p.right);
        }
    }

    public static void inOrderStack(TreeNode p) {
        Stack<TreeNode> stack = new Stack<>();
        while (p != null || !stack.empty()) {
            while (p != null) {
                stack.push(p);
                p = p.left;
            }
            if (!stack.empty()) {
                p = stack.pop();
                visit(p);
                p = p.right;
            }
        }
    }
}
