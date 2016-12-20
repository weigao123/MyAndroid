package com.garfield.weishu.datastructure.linearlist;

/**
 * Created by gaowei3 on 2016/12/2.
 */

public class SingleLinkedList<T> implements ILinearList<T> {

    private Node head = null; // 头节点

    private class Node {
        Node next;
        T data;
        public Node(T data) {
            this.data = data;
        }
    }

    @Override
    public boolean insert(int index, T elem) {
        Node newNode = new Node(elem);
        if (head == null) {
            head = newNode;
            return true;
        }
        Node tmp = head;
        while (tmp.next != null) {
            tmp = tmp.next;
        }
        tmp.next = newNode;
        return true;
    }

    @Override
    public boolean delete(int index) {
        if (index < 1 || index > length()) {
            return false;
        }
        return false;
    }

    @Override
    public T get(int index) {
        return null;
    }

    @Override
    public int locate(T elem) {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public int length() {
        int sum = 0;
        return 0;
    }
}
