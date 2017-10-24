package com.garfield.java.datastructure.tree;

import java.util.Stack;

import static com.garfield.java.datastructure.tree.TreeNode.visit;

/**
 * Created by gaowei on 2017/10/24.
 */

public class TreePostOrder {

    public static void postOrderRec(TreeNode p) {
        if (p != null) {
            postOrderRec(p.left);
            postOrderRec(p.right);
            visit(p);
        }
    }

    /**
     * 增加一个标记，判断右结点访问完了才能出栈
     *
     * 1、入栈，所有左元素依次入栈
     * 2、出栈，根据标记让能出栈的全出栈，访问出栈的元素，从L依次到I
     * 2、不出栈，添加标记，指向其右元素，继续1
     * 3、两个while循环，第一层是依次入栈所有的左元素，第二层是依次出栈并指向其右元素直到右元素非空
     */
    public static void postOrderStack(TreeNode p) {
        Stack<TreeNode> stack = new Stack<>();
        Stack<Boolean> tag = new Stack<>();
        while (p != null || !stack.empty()) {
            while (p != null) {
                stack.push(p);
                tag.push(false);
                p = p.left;
            }
            // 必须得用while，把所有已标记的弹出
            while (!stack.empty() && tag.peek().equals(true)) {
                tag.pop();
                visit(stack.pop());
            }
            if (!stack.empty()) {
                // 添加标记
                tag.pop();
                tag.push(true);
                // 不出栈
                p = stack.peek();
                p = p.right;
            }
        }
    }

    // 不用标记，前序遍历反过来
    public static void postOrder3(TreeNode p) {
        Stack<TreeNode> stack = new Stack<>();
        Stack<TreeNode> result = new Stack<>();
        while (p != null || !stack.empty()) {
            while (p != null) {
                stack.push(p);
                result.push(p);
                p = p.right;
            }
            if (!stack.empty()) {
                p = stack.pop();
                p = p.left;
            }
        }
        while (!result.empty()) {
            visit(result.pop());
        }
    }
}
