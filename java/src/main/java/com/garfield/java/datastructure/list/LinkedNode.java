package com.garfield.java.datastructure.list;

/**
 * Created by gaowei3 on 2017/3/16.
 */

public class LinkedNode {

    public LinkedNode next;
    public int value;

    public LinkedNode(int value, LinkedNode next) {
        this.value = value;
        this.next = next;
    }

    public static LinkedNode generateList() {
        LinkedNode node6 = new LinkedNode(6, null);
        LinkedNode node5 = new LinkedNode(5, node6);
        LinkedNode node4 = new LinkedNode(4, node5);
        LinkedNode node3 = new LinkedNode(3, node4);
        LinkedNode node2 = new LinkedNode(2, node3);
        LinkedNode node1 = new LinkedNode(1, node2);
        return node1;
    }

    public static LinkedNode generateCross1() {
        LinkedNode node7 = new LinkedNode(7, null);
        LinkedNode node5 = new LinkedNode(5, node7);
        LinkedNode node3 = new LinkedNode(3, node5);
        LinkedNode node1 = new LinkedNode(1, node3);
        return node1;
    }

    public static LinkedNode generateCross2() {
        LinkedNode node8 = new LinkedNode(8, null);
        LinkedNode node6 = new LinkedNode(6, node8);
        LinkedNode node4 = new LinkedNode(4, node6);
        LinkedNode node2 = new LinkedNode(2, node4);
        return node2;
    }

}
