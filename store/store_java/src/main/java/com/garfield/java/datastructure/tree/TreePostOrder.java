package com.garfield.java.datastructure.tree;

import com.garfield.java.util.L;

import java.util.Stack;

/**
 * Created by gaowei on 2017/10/24.
 */

public class TreePostOrder {

    public static void postOrderRec(TreeNode p) {
        if (p != null) {
            postOrderRec(p.left);
            postOrderRec(p.right);
            L.dl(p);
        }
    }

    /**
     * 增加一个标记，判断右结点访问完了才能出栈
     *
     * 1、入栈，所有左元素依次入栈
     * 2、出栈，根据标记让能出栈的全出栈，访问出栈的元素，从L依次到I
     * 3、不出栈，添加标记，指向其右元素，继续1
     */
    public static void postOrderStack(TreeNode p) {
        Stack<TreeNode> stack = new Stack<>();
        Stack<Boolean> tag = new Stack<>();
        while (p != null || !stack.empty()) {
            while (p != null) {
                stack.push(p);
                tag.push(false);  // 第一次访问
                p = p.left;
            }
            // 必须得用while，把所有【已标记】的弹出
            // tag==true，说明是第三次访问，即后序
            while (!stack.empty() && tag.peek().equals(true)) {
                tag.pop();
                L.dl(stack.pop());     // 第三次访问，打印
            }
            if (!stack.empty()) {
                tag.pop();
                tag.push(true);    // 第二次访问，修改tag
                p = stack.peek();  // 不能弹出，因为一会还要再访问
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
            L.dl(result.pop());
        }
    }
}
