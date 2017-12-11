package com.garfield.java.datastructure.sort;

/**
 * Created by gaowei on 17/1/2.
 */

public class InsertSort implements ISort {

    /**
     * 插入排序
     */
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();

        int size = array.length;
        // size-1个数需要被插入
        for (int i = 1; i < size; i ++) {
            // 下面才是直接插入排序的核心
            // i前面的数都是已经排好序的，所以比i-1大就比前面所有的大
            if (array[i] >= array[i - 1])
                continue;
            int target = array[i];
            int j;
            // 从i-1开始遍历前面所有的数，只要有比i大的就后移，
            // target < array[j] 条件写在for循环里更简洁
            for (j = i - 1; j >= 0 && target < array[j]; j --) {
                array[j + 1] = array[j];
            }
            // 插入到空位
            array[j + 1] = target;
        }
        return System.currentTimeMillis() - current;
    }
}
