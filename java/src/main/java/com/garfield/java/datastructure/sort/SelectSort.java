package com.garfield.java.datastructure.sort;

import com.garfield.java.datastructure.util.ArrayUtils;

/**
 * Created by gaowei on 17/1/2.
 */

public class SelectSort implements ISort {

    /**
     * 选择排序
     * O(n^2), O(1), 不稳定
     */
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();

        int size = array.length;
        for (int i = 0; i < size - 1; i++) {   // 和冒泡一样，需要size-1轮，把size-1个泡泡冒出
            int minIndex = i;    // 先默认第一个是最小的
            for (int j = size - 1; j >= i + 1; j--) {   // 和冒泡一样
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                ArrayUtils.swap(array, minIndex, i);
            }
        }
        return System.currentTimeMillis() - current;
    }
}


