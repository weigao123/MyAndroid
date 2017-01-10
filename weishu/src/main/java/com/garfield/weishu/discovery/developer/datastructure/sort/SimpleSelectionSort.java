package com.garfield.weishu.discovery.developer.datastructure.sort;

import com.garfield.baselib.utils.array.ArrayUtils;

/**
 * Created by gaowei on 17/1/2.
 */

public class SimpleSelectionSort implements ISort {

    /**
     * 简单选择排序
     * 每一轮把最大的值放最下面，排序完从小到大
     */
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();

        int size = array.length;
        // 需要size-1轮
        for (int i = 0; i < size - 1; i ++) {
            // 认为最大的在最后一位
            int max = size - 1 - i;
            // 从0~max前一个(max-1)，依次与max位比较大小
            for (int j = 0; j < size - 1 - i; j ++) {
                if (array[max] < array[j]) {
                    max = j;
                }
            }
            if (max != size - 1 - i) {
                ArrayUtils.swap(array, max, size - 1 - i);
            }
        }
        return System.currentTimeMillis() - current;
    }
}
