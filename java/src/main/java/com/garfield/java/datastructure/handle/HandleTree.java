package com.garfield.java.datastructure.handle;

import com.garfield.java.datastructure.tree.TreeNode;
import com.garfield.java.datastructure.util.ArrayUtils;
import com.garfield.java.util.L;

import java.util.Stack;

/**
 * Created by gaowei on 2018/1/23.
 */

public class HandleTree {


    public static void test() {

        TreeNode head = TreeNode.generateTree();

        //包含子树
        //L.d(hasSubTree(head, TreeNode.generateSubTree()));

        //二叉树镜像
        //mirrorTree(head);

        //查找二叉树路径和的值
        //findPath(head, 'A'+'B'+'D'+'H');
    }

    /**——————————————————————————————是否包含子树————————————————————————————————————————————*/
    private static boolean hasSubTree(TreeNode node1, TreeNode node2) {
        if (node1 == null || node2 == null) {
            return false;
        }
        boolean result = false;
        if (node1.value == node2.value) {
            result = isSameSub(node1, node2);
        }
        if (!result) {
            result = hasSubTree(node1.left, node2);
        }
        if (!result) {
            result = hasSubTree(node1.right, node2);
        }
        return result;
    }
    private static boolean isSameSub(TreeNode node1, TreeNode node2) {
        if (node2 == null) {
            return true;
        } else if (node1 == null) {
            return false;
        }
        boolean result = false;
        if (node1.value == node2.value) {
            result = true;
        }
        if (result) {
            result = isSameSub(node1.left, node2.left);
        }
        if (result) {
            result = isSameSub(node1.right, node2.right);
        }
        return result;
    }

    /**——————————————————————————————二叉树的镜像——————————————————————————————————————————*/
    private static void mirrorTree(TreeNode pHead) {
        if (pHead == null) {
            return;
        }
        // 放前放后都可以
        mirrorTree(pHead.left);
        mirrorTree(pHead.right);
        TreeNode tmp = pHead.left;
        pHead.left = pHead.right;
        pHead.right = tmp;
    }

    /**——————————————————————————————之形遍历二叉树—————————————————————————————————————————*/

    /**——————————————————————————————找到二叉树的某个路径和是某个值———————————————————————————*/
    private static void findPath(TreeNode pHead, int t) {
        if (pHead == null) return;
        Stack<Character> stack = new Stack<>();
        findPathInner(pHead, stack, t);
    }
    // 先序遍历
    // 叶子结点就是左孩子和右孩子同时是空
    // 每次开启栈帧时，保存结点，销毁栈帧时移除结点，所以这个栈就一直有从根结点到当前结点的链路
    // 想象成一个串行栈帧序列
    private static void findPathInner(TreeNode node, Stack<Character> stack, int t) {
        if (node == null) {
            return;
        }

        // 开启栈帧，保存结点
        char value = node.value;
        stack.push(value);

        // 这里可以打印出根节点到每一个结点的路径
        // 每一次执行到一个栈帧时，左右的栈帧要么还没开启，要么已经销毁，所以当前肯定是尾部
        // ArrayUtils.printStack(stack);

        // 发现是叶子结点，必须放到push下面
        if (node.left == null && node.right == null) {
            if (ArrayUtils.calStack(stack) == t) {
                ArrayUtils.printStack(stack);
                L.d("ok");
            }
        }

        findPathInner(node.left, stack, t);
        findPathInner(node.right, stack, t);

        // 销毁栈帧，每次销毁栈帧就说明这一帧的保存的结点生命结束
        stack.pop();
    }

    /**——————————————————————————————二叉搜索树转换成双向链表————————————————————————————*/

    /**——————————————————————————————判断是否平衡二叉树——————————————————————————————————*/
    // 任意结点的左右子树深度相差不超过1

}
