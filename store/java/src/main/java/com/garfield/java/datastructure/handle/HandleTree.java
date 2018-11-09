package com.garfield.java.datastructure.handle;

import com.garfield.java.datastructure.tree.TreeNode;
import com.garfield.java.datastructure.util.ArrayUtils;
import com.garfield.java.util.L;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by gaowei on 2018/1/23.
 */

public class HandleTree {


    public static void test() {

        TreeNode head = TreeNode.generateTree();

        //包含子树
        //L.dl(hasSubTree(head, TreeNode.generateSubTree()));

        //二叉树镜像
        //mirrorTree(head);

        //查找二叉树路径和的值
        //findPath(head, 'A'+'B'+'D'+'H');

        //L.dl(getNodeNumOfFloorRec(head, 2));

        //L.dl(TreeDepth.getDepthQueue(head));
    }

    /**——————————————————————————————是否包含子树————————————————————————————————————————————*/
    // 其实就是先序遍历所有的node1，依次与node2比较
    private static boolean hasSubTree(TreeNode node1, TreeNode node2) {
        if (node1 == null || node2 == null) {
            return false;
        }
        boolean result = isSameSub(node1, node2);
        if (!result) {
            result = hasSubTree(node1.left, node2);   //条件遍历
        }
        if (!result) {
            result = hasSubTree(node1.right, node2);
        }
        return result;
    }
    // 两个头结点对齐，是否是子树
    private static boolean isSameSub(TreeNode node1, TreeNode node2) {
        if (node2 == null) {
            return true;
        } else if (node1 == null) {
            return false;
        }
        if (node1.value != node2.value) {
            return false;
        }
        return isSameSub(node1.left, node2.left) && isSameSub(node1.right, node2.right);
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
    // 递归形式的先序遍历，也可以求深度？
    // 叶子结点就是左孩子和右孩子同时是空
    // 每次开启栈帧时，保存结点，销毁栈帧时移除结点，所以这个栈就一直有从根结点到当前结点的链路
    // 想象成一个串行栈帧序列
    private static void findPathInner(TreeNode node, Stack<Character> stack, int t) {
        if (node == null) {
            return;
        }

        // 开启栈帧，保存结点
        stack.push(node.value);

        // 这里可以打印出根节点到每一个结点的路径
        // 每一次执行到一个栈帧时，左右的栈帧要么还没开启，要么已经销毁，所以当前肯定是尾部
        // ArrayUtils.printStack(stack);

        // 发现是叶子结点，必须放到push下面
        if (node.left == null && node.right == null) {
            if (ArrayUtils.calStack(stack) == t) {
                ArrayUtils.printStack(stack);
                L.dl("ok");
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


    /**——————————————————————————————求第k层结点个数(从0开始)————————————————————————————*/
    // 求第5层的结点数 = 以左结点为根求第4层结点数 + 以右结点为根求第4层结点数
    // 还是要分而治之
    private static int getNodeNumOfFloorRec(TreeNode node, int k) {
        if (node == null) {
            return 0;
        }
        if (k == 0) {
            return 1;
        }
        return getNodeNumOfFloorRec(node.left, k - 1) + getNodeNumOfFloorRec(node.right, k - 1);
    }
    // 利用层级求深度
    private static int getNodeNumOfFloor(TreeNode node, int k) {
        if (node == null) {
            return 0;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(node);
        int cur, last;
        int floor = -1;   //正在遍历的当前层
        while (!queue.isEmpty()) {
            ++ floor;
            if (floor == k) {
                break;
            }
            cur = 0;                 //本层已经遍历的个数
            last = queue.size();     //当前层的总数
            while (cur < last) {     //弹出这一层的同时，把下一层的元素加入
                node = queue.poll();
                ++ cur;
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }

        }
        return queue.size();
    }

}
