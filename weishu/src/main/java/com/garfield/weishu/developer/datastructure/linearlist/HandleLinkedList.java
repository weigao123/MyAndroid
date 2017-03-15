package com.garfield.weishu.developer.datastructure.linearlist;

/**
 * Created by gaowei3 on 2017/3/16.
 */

public class HandleLinkedList {



    /**
     * 链表反转
     */
    public static Node reverse(Node current) {
        Node previousNode = null;
        Node nextNode = null;

        while (current != null) {
            nextNode = current.next;     //移动next
            current.next = previousNode;
            previousNode = current;      //移动pre
            current = nextNode;          //移动current
        }
        return previousNode;
    }

    /**
     * 链表反转，递归
     */
    public static Node reverseRecursion(Node current) {
        if (current == null || current.next == null) {
            return current;
        }
        Node nextNode = current.next;
        current.next = null;
        Node reverseRest = reverse(nextNode);
        nextNode.next = current;
        return reverseRest;
    }
}
