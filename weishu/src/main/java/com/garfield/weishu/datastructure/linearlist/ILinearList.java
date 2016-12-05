package com.garfield.weishu.datastructure.linearlist;

/**
 * Created by gaowei3 on 2016/12/2.
 */

public interface ILinearList<T> {
    void clear();
    T get(int index);
    int locate(T elem);
    boolean insert(int index, T elem);
    boolean delete(int index);
    int length();

    String toString();
}
