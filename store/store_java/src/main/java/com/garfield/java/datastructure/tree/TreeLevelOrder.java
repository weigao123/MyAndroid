package com.garfield.java.datastructure.tree;

import com.garfield.java.util.L;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by gaowei on 2017/10/24.
 */

public class TreeLevelOrder {

    /**
     * 层级遍历，把每一层都连在一起，好像一条直线
     */
    public static void levelOrderQueue(TreeNode p) {
        if (p == null) {
            return;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(p);
        // 【一边出队列一边进队列】，都把它们的孩子再放入队列
        // 一轮while，仅遍历一个元素，加入两个子元素，无法判断层级
        while (!queue.isEmpty()) {
            p = queue.poll();
            L.dl(p);
            if (p.left != null) {
                queue.offer(p.left);
            }
            if (p.right != null) {
                queue.offer(p.right);
            }
        }
    }
}
