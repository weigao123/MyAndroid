package com.garfield.java.datastructure.handle;

import com.garfield.java.datastructure.list.LinkedNode;
import com.garfield.java.util.L;

/**
 * Created by gaowei3 on 2017/3/16.
 */

public class HandleLinked {

    public static void test() {

        LinkedNode pHead = LinkedNode.generateList();

        //链表反向打印
        //reversePrintRec(pHead);

        //链表反转
        //printRec(reverse2(pHead));

        //链表合并
        //print(mergeRec(LinkedNode.generateCross1(), LinkedNode.generateCross2()));

        //倒数第k个结点
        //L.d(getKBack(pHead, 20));
    }

    /**——————————————————————————————正向打印————————————————————————————————————————————————*/
    private static void print(LinkedNode pHead) {
        LinkedNode curNode = pHead;
        while (curNode != null) {
            L.d(curNode.value);
            curNode = curNode.next;
        }

    }
    private static void printRec(LinkedNode pHead) {
        if (pHead != null) {
            L.d(pHead.value);
            printRec(pHead.next);
        }
    }
    /**——————————————————————————————反向打印————————————————————————————————————————————————*/
    private static void reversePrintRec(LinkedNode pHead) {
        // 终止条件在最上面
        if (pHead == null) {
            return;
        }
        reversePrintRec(pHead.next);
        L.d(pHead.value);
    }
    /**——————————————————————————————链表反转————————————————————————————————————————————————*/
    private static LinkedNode reverseRec(LinkedNode pHead) {
        // 终止条件在最上面
        // pHead.next == null 用于尾巴的边界值，到这就可以停了，让头指针指向它
        // pHead == null 用于开头的边界值
        if (pHead == null || pHead.next == null) {
            return pHead;
        }
        // 依次把新的结点传进去
        LinkedNode newParent = reverseRec(pHead.next);
        pHead.next.next = pHead;
        pHead.next = null;    //必须得置空，因为原来的头，否则循环引用
        return newParent;
    }

    // 首先需要有一个指针去遍历，当反转时
    // 需要有一个指针保留前一个结点，否则没法反转
    // 需要有一个指针保存后一个结点，否则就断了
    // 所以需要三个指针
    private static LinkedNode reverse(LinkedNode pHead) {
        LinkedNode preNode = null;    //指向第一个的前一个，即使出界也没关系
        LinkedNode curNode = pHead;
        LinkedNode nextNode;          //先不做初始化，尽量放到判空循环里去做！
        while (curNode != null) {     //技巧性太强了
            nextNode = curNode.next;  //移动n
            curNode.next = preNode;   //反转
            preNode = curNode;        //移动p
            curNode = nextNode;       //移动c
        }
        return preNode;
    }
    private static LinkedNode reverse2(LinkedNode pHead) {
        if (pHead == null) return null;
        LinkedNode p = null;
        LinkedNode c = pHead;
        LinkedNode n = pHead.next;
        while (c != null) {
            // 干净操作，先反转c，再所有一起前移，停止点是c==null
            // 但是c非空n可能空，导致反转完再移动时空指针，又需要一次判空
            c.next = p;
            if (n == null) break;
            p = c;
            c = n;
            n = n.next;
        }
        return c;
    }
    /**——————————————————————————————链表倒数第k个结点———————————————————————————————————————*/
    private static int getKBack(LinkedNode pHead, int k) {
        if (pHead == null || k <= 0) return -1;
        LinkedNode p = pHead, c = pHead;
        int step = 0;
        while (c != null) {
            if (step == k) {
                c = c.next;
                p = p.next;
            } else {
                c = c.next;
                ++step;
            }
        }
        return step < k ? -1 : p.value;
    }
    /**——————————————————————————————两个链表的第一个公共结点———————————————————————————————*/
    // 最终肯定是Y型结构
    // 方法1：暴力比较，O(mn)
    // 方法2：倒着比较，用栈，O(m+n)+O(m+n)
    // 方法3：先计算长度差，然后同时前进，找到第一个相同的结点，O(m+n)


    /**——————————————————————————————链表合并——————————————————————————————————————————————*/
    private static LinkedNode mergeRec(LinkedNode pHead1, LinkedNode pHead2) {
        if (pHead1 == null) {
            return pHead2;
        }
        if (pHead2 == null) {
            return pHead1;
        }
        LinkedNode pNewHead = null;
        if (pHead1.value < pHead2.value) {
            pNewHead = pHead1;
            pNewHead.next = mergeRec(pHead1.next, pHead2);
        } else {
            pNewHead = pHead2;
            pNewHead.next = mergeRec(pHead1, pHead2.next);
        }
        return pNewHead;
    }

    private static LinkedNode merge(LinkedNode pHead1, LinkedNode pHead2) {
        if (pHead1 == null) {
            return pHead2;
        }
        if (pHead2 == null) {
            return pHead1;
        }
        LinkedNode node1 = pHead1;
        LinkedNode node2 = pHead2;
        LinkedNode pHeadNew;
        LinkedNode nodeNew;
        if (node1.value > node2.value) {
            pHeadNew = nodeNew = node2;
        } else {
            pHeadNew = nodeNew = node1;
        }

        while (node1 != null && node2 != null) {
            if (node1.value >= node2.value) {

                nodeNew.next = node2;
                nodeNew = node2.next;
            } else {
                node1 = node1.next;
                node1.next = node2;

            }
        }
        return pHeadNew;
    }



}
