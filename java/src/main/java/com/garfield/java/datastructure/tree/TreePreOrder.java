package com.garfield.java.datastructure.tree;

import java.util.Stack;

import static com.garfield.java.datastructure.tree.TreeNode.visit;

/**
 * Created by gaowei on 2017/10/24.
 */

public class TreePreOrder {

    public static void preOrderRec(TreeNode p) {
        if (p != null) {
            visit(p);
            preOrderRec(p.left);
            preOrderRec(p.right);
        }
    }

    /**
     * 1、入栈，所有左元素依次入栈
     * 2、出栈，指向其右元素，继续1
     * 3、两个while循环，第一层是依次入栈所有的左元素，第二层是依次出栈直到右元素非空
     */
    public static void preOrderStack(TreeNode p) {
        Stack<TreeNode> stack = new Stack<>();
        // 有新一级或者栈里有元素
        while (p != null || !stack.empty()) {
            // 遍历自己以及左侧全部，依次入栈
            while (p != null) {
                // 入栈时访问
                stack.push(p);
                visit(p);
                p = p.left;
            }
            // 出栈并拿到右元素，新一级的首元素
            if (!stack.empty()) {
                p = stack.pop();
                p = p.right;
            }
        }
    }

    /**
     * 把每一层都连在一起，好像一条直线
     *

     * 维护一个栈，将根节点入栈，然后只要栈不为空，出栈并访问，接着依次将访问节点的右节点、左节点入栈。
     * 这种方式应该是对先序遍历的一种特殊实现（看上去简单明了），但是不具备很好的扩展性，在中序和后序方式中不适用
     */
    public static void preOrderStack2(TreeNode p){
        if (p == null) return;
        Stack<TreeNode> s = new Stack<>();
        s.push(p);
        while (!s.isEmpty()) {
            p = s.pop();
            visit(p);
            if (p.right != null) s.push(p.right);
            if (p.left != null) s.push(p.left);
        }
    }
}
