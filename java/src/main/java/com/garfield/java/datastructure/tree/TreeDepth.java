package com.garfield.java.datastructure.tree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by gaowei on 2017/10/24.
 */

public class TreeDepth {

    /**
     * 递归求深度
     */
    public static int getDepthRec(TreeNode p) {
        if (p == null)
            return 0;

        int leftDeep = getDepthRec(p.left);
        int rightDeep = getDepthRec(p.right);
        return leftDeep > rightDeep ? leftDeep + 1 : rightDeep + 1;
    }

    /**
     * 利用非递归遍历树时，后出栈元素的方法
     */
    public static int getDepthStack(TreeNode p) {
        int height = 0;
        Stack<TreeNode> stack = new Stack<>();
        Stack<Boolean> flag = new Stack<>();
        while (p != null || !stack.empty()) {
            while (p != null) {
                stack.push(p);
                flag.push(false);
                p = p.left;
                height = stack.size() > height ? stack.size() : height;
            }
            while (!stack.empty() && flag.peek().equals(true)) {
                flag.pop();
                stack.pop();
            }
            if (!stack.empty()) {
                flag.pop();
                flag.push(true);
                p = stack.peek();
                p = p.right;
            }
        }
        return height;
    }

    /**
     * 利用非递归层级遍历
     */
    public static int getDepthQueue(TreeNode p) {
        int deep = 0;
        if (p == null) {
            return deep;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(p);
        int cur, last;
        while (!queue.isEmpty()) {
            cur = 0;                 //本层已经遍历的个数
            last = queue.size();     //当前层的总数
            while (cur < last) {
                p = queue.poll();
                ++ cur;
                if (p.left != null) {
                    queue.offer(p.left);
                }
                if (p.right != null) {
                    queue.offer(p.right);
                }
            }
            ++ deep;
        }
        return deep;
    }
}
