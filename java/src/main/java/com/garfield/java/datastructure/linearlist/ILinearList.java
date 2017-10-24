package com.garfield.java.datastructure.linearlist;

/**
 * Created by gaowei3 on 2016/12/2.
 */

public interface ILinearList<T> {

    /**
     * 增
     */
    boolean insert(int index, T elem);

    /**
     * 删
     */
    boolean delete(int index);

    /**
     * 查
     */
    T get(int index);

    /**
     * 定
     */
    int locate(T elem);

    /**
     * 清
     */
    void clear();

    int length();
    String toString();
}
