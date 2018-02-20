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
     * 利用非递归，深度优先(后序)遍历，查找栈元素最大数量
     * 使用先序递归，可以吗？
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
                height = stack.size() > height ? stack.size() : height;   //在入栈时计算高度
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
        if (p == null) {
            return 0;
        }
        int deep = 0;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(p);
        // 一轮while循环，遍历该层所有的元素
        while (!queue.isEmpty()) {
            ++deep;                            //正在遍历的当前深度
            int size = queue.size();           //现在的queue全都是这一层的，必须记住这一层的size
            for (int i = 0; i < size; i++) {   //把这一层全弹出来
                p = queue.poll();
                if (p.left != null) {
                    queue.offer(p.left);
                }
                if (p.right != null) {
                    queue.offer(p.right);
                }
            }
        }
        return deep;
    }

    public static int minDeep(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int left = minDeep(node.left);
        int right = minDeep(node.right);
        return left < right ? left + 1 : right + 1;
    }
}
