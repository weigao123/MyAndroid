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
     * 利用非递归，深度优先遍历
     * 后出栈元素，查找栈元素最大数量
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
     * 利用非递归，广度优先遍历
     * 查找遍历的层数
     * 两层while，第一层遍历层数，第二层遍历当前层
     */
    public static int getDepthQueue(TreeNode p) {
        int deep = 0;
        if (p == null) {
            return deep;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(p);
        // 必须要增加两个指针，用来判断是否在同一层
        int cur, last;
        while (!queue.isEmpty()) {
            cur = 0;                 //本层已经遍历的个数
            last = queue.size();     //当前层的总数
            while (cur < last) {     //弹出这一层的同时，把下一层的元素加入
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
