package com.garfield.java.datastructure.tree;

import com.garfield.java.util.L;

import java.util.Stack;

/**
 * Created by gaowei on 2017/10/24.
 */

public class TreePreOrder {

    /**
     * <关于递归>
     * 每一层栈帧都持有一个结点，递归的本质就是把新的结点放到新的栈帧里
     * 单线程，同时只有一个栈帧在执行，串行
     *
     * <父结点和子结点的关系>
     * 一个子结点去到另一个子结点时肯定要经过父结点，所以可以有3个切入点
     * 每一次执行到一个栈帧时，左右的栈帧要么还没开启，要么已经销毁，所以当前肯定是尾部
     * 而且3个切入点，每个位置都只会执行一遍
     *
     * 1、创建栈帧
     * 每次方法调用，就创建一个栈帧，根节点一路向左下，一直新建栈帧
     *
     * 2、销毁栈帧
     * 每次方法执行完，就回到上一层虚拟机栈帧，这里持有着当时的结点p，要么再递归新建栈帧，要么关闭当前栈帧
     *
     */
    public static void preOrderRec(TreeNode p) {
        // 开启栈帧
        if (p != null) {
            L.dl(p);                 //当前栈帧的处理
            preOrderRec(p.left);    //左结点放到新栈帧
            preOrderRec(p.right);   //右结点放到新栈帧
        }
        // 方法执行完关闭栈帧
    }

    /**
     * 1、非空就入栈并指向左孩子，动一排元素
     * 2、出栈并指向该元素的右孩子，只动一个元素
     */
    public static void preOrderStack(TreeNode p) {
        Stack<TreeNode> stack = new Stack<>();
        // 有新一级或者栈里有元素
        while (p != null || !stack.empty()) {
            // 遍历自己以及左侧全部，依次入栈
            while (p != null) {
                // 入栈时访问
                stack.push(p);
                L.dl(p);
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
    public static void preOrderStack2(TreeNode p) {
        if (p == null) return;
        Stack<TreeNode> s = new Stack<>();
        s.push(p);
        while (!s.isEmpty()) {
            p = s.pop();
            L.dl(p);
            if (p.right != null) s.push(p.right);    //必须先放入右
            if (p.left != null) s.push(p.left);
        }
    }
}
