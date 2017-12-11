package com.garfield.java.datastructure.sort;

import com.garfield.java.datastructure.util.ArrayUtils;

/**
 * Created by gaowei on 17/1/2.
 */

public class SelectSort implements ISort {

    /**
     * 选择排序
     */
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();

        int size = array.length;
        // 需要size-1轮
        for (int i = 0; i < size - 1; i ++) {
            // 认为最大的在最后一位
            int maxIndex = size - 1 - i;
            // 从0~max前一个(max-1)，依次与max位比较大小
            for (int j = 0; j < size - 1 - i; j ++) {
                if (array[maxIndex] < array[j]) {
                    maxIndex = j;
                }
            }
            if (maxIndex != size - 1 - i) {
                ArrayUtils.swap(array, maxIndex, size - 1 - i);
            }
        }
        return System.currentTimeMillis() - current;
    }
}
