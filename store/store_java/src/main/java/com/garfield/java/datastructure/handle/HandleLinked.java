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
        //printRec(reverse33(pHead));

        //链表合并
        //print(merge2(LinkedNode.generateCross1(), LinkedNode.generateCross2()));

        //倒数第k个结点
        //L.dl(getKBack(pHead, 20));

        //大数相加
        //print(bigNumAdd(LinkedNode.generateCross1(), LinkedNode.generateCross2()));
    }

    /**——————————————————————————————正向打印————————————————————————————————————————————————*/
    private static void print(LinkedNode pHead) {
        LinkedNode curNode = pHead;
        while (curNode != null) {
            L.dl(curNode.value);
            curNode = curNode.next;
        }

    }
    private static void printRec(LinkedNode pHead) {
        if (pHead != null) {
            L.dl(pHead.value);
            printRec(pHead.next);
        }
    }
    /**——————————————————————————————反向打印————————————————————————————————————————————————*/
    // 栈、递归
    private static void reversePrintRec(LinkedNode pHead) {
        // 终止条件在最上面
        if (pHead == null) {
            return;
        }
        reversePrintRec(pHead.next);
        L.dl(pHead.value);
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
        pHead.next = null;    //必须得置空，因为原来的头最后必须指null，否则循环引用
        return newParent;     //返回值要透传
    }

    // 首先需要有一个指针去遍历，当反转时
    // 需要有一个指针保留前一个结点，否则没法反转
    // 需要有一个指针保存后一个结点，否则就断了
    // 所以需要三个指针
    private static LinkedNode reverse(LinkedNode pHead) {
        LinkedNode preNode = null;    //指向第一个的前一个，即使出界也没关系
        LinkedNode curNode = pHead;
        LinkedNode nextNode = null;   //先不做初始化，尽量放到判空循环里去做！
        while (curNode != null) {     //技巧性太强了
            // 4行，1个反转，3个移动
            nextNode = curNode.next;  //【先保存下一个p】，因为要断开了
            curNode.next = preNode;   //反转
            preNode = curNode;        //先移动最前面的p
            curNode = nextNode;       //再移动中间的c
        }
        return preNode;
    }
    private static LinkedNode reverse2(LinkedNode pHead) {
        if (pHead == null) return null;
        LinkedNode p = null;
        LinkedNode c = pHead;   // 进while循环之前一定要已经初始化
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

    /**——————————————————————————————是否有环———————————————————————————————————————————————*/
    // 也可以用HashMap实现，next指向曾经出现过就有环
    private static boolean hasCycle(LinkedNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        LinkedNode slow, fast;
        // 初始化时先错开
        fast = head.next;
        slow = head;
        // 每次while两者距离差会+1，无论一开始间距多少，距离差迟早=环周长，相遇
        while (fast != slow) {
            if (fast == null || fast.next == null) {
                return false;
            }
            fast = fast.next.next;   //走两步
            slow = slow.next;
        }
        return true;
    }
    /**——————————————————————————————链表排序———————————————————————————————————————————————*/


    /**——————————————————————————————大数，两个链表相加———————————————————————————————————————*/
    // 243 + 5641 = 5884
    // 链表顺序，2->4->3 + 5->6->4->1 = 5->8->8->4
    // 可以递归处理

    // 链表是逆序，从个位数开始，3->4->2 + 1->4->6->5 = 4->8->8->5
    private static LinkedNode bigNumAdd(LinkedNode node1, LinkedNode node2) {
        int carry = 0;
        LinkedNode pNewHead = new LinkedNode(0);
        LinkedNode cur = pNewHead;
        // while前，要初始化
        while (node1 != null || node2 != null || carry != 0) {    //carry写这里便捷
            int x = 0;
            if (node1 != null) {
                x = node1.value;
                node1 = node1.next;
            }
            int y = 0;
            if (node2 != null) {
                y = node2.value;
                node2 = node2.next;
            }
            cur.next = new LinkedNode((x + y + carry) % 10);
            cur = cur.next;    //让cur指向刚建的结点
            carry = (x + y) / 10;
        }
        return pNewHead.next;
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


    /**——————————————————————————————有序链表合并———————————————————————————————————————————*/
    // 每一轮循环，返回的都是合并后的头结点
    private static LinkedNode mergeRec(LinkedNode pHead1, LinkedNode pHead2) {
        if (pHead1 == null) {
            return pHead2;
        }
        if (pHead2 == null) {
            return pHead1;
        }
        LinkedNode pNewHead;
        if (pHead1.value < pHead2.value) {
            pNewHead = pHead1;
            pNewHead.next = mergeRec(pHead1.next, pHead2);
        } else {
            pNewHead = pHead2;
            pNewHead.next = mergeRec(pHead1, pHead2.next);
        }
        return pNewHead;
    }

    private static LinkedNode merge(LinkedNode p1, LinkedNode p2) {
        LinkedNode pNewHead = new LinkedNode(0);   //为了while循环内形式统一
        LinkedNode cur = pNewHead;
        // 进while循环之前一定要已经初始化，先让cur指向一个结点
        while (p1 != null && p2 != null) {
            if (p1.value < p2.value) {
                cur.next = p1;
                cur = cur.next;
                p1 = p1.next;
            } else {
                cur.next = p2;
                cur = cur.next;
                p2 = p2.next;
            }
        }
        if (p1 != null) {
            cur.next = p1;
        } else {
            cur.next = p2;
        }
        return pNewHead.next;
    }
    private static LinkedNode merge2(LinkedNode p1, LinkedNode p2) {
        if (p1 == null) {
            return p2;
        }
        if (p2 == null) {
            return p1;
        }
        LinkedNode pNewHead;
        LinkedNode cur;
        if (p1.value < p2.value) {
            cur = pNewHead = p1;
            p1 = p1.next;
        } else {
            cur = pNewHead = p2;
            p2 = p2.next;
        }
        // 进while循环之前一定要已经初始化，先让cur指向一个结点
        while (p1 != null && p2 != null) {
            if (p1.value < p2.value) {
                cur.next = p1;
                cur = cur.next;
                p1 = p1.next;
            } else {
                cur.next = p2;
                cur = cur.next;
                p2 = p2.next;
            }
        }
        if (p1 != null) {
            cur.next = p1;
        } else {
            cur.next = p2;
        }
        return pNewHead;
    }


}
