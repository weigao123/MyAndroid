package com.garfield.java.datastructure.sort;

/**
 * Created by gaowei on 17/1/2.
 */

public class InsertSort implements ISort {

    /**
     * 插入排序
     * O(n^2), O(1), 稳定
     */
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();

        int size = array.length;
        for (int i = 1; i < size; i++) {   // [1, size-1]
            if (array[i] >= array[i - 1])
                continue;
            int tmp = array[i];
            int j;
            // j初始指向第一个大于i位置的指针
            // 后面只用到了j和j+1，所以j>=0即可
            for (j = i - 1; j >= 0 && tmp < array[j]; j--) {
                array[j + 1] = array[j];
            }
            array[j + 1] = tmp;
//            for (j = i; j >= 1 && tmp < array[j-1]; j--) {
//                array[j] = array[j-1];
//            }
//            array[j] = tmp;
        }
        return System.currentTimeMillis() - current;
    }
}
