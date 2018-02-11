package com.garfield.java.datastructure.tree;

import com.garfield.java.util.L;

import java.util.Stack;

/**
 * Created by gaowei on 2017/10/24.
 */

public class TreeInOrder {

    /**
     * 中序遍历
     */
    public static void inOrderRec(TreeNode p) {
        if (p != null) {
            inOrderRec(p.left);
            L.dl(p);
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
                L.dl(p);
                p = p.right;
            }
        }
    }
}
