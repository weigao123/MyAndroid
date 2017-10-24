package com.garfield.java.datastructure.tree;

import java.util.LinkedList;
import java.util.Queue;

import static com.garfield.java.datastructure.tree.TreeNode.visit;

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
        while (!queue.isEmpty()) {
            p = queue.poll();
            visit(p);
            if (p.left != null) {
                queue.offer(p.left);
            }
            if (p.right != null) {
                queue.offer(p.right);
            }
        }
    }
}
