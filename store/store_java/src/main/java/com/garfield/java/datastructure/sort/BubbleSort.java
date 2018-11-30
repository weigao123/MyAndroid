package com.garfield.java.datastructure.sort;


import com.garfield.java.datastructure.util.ArrayUtils;

/**
 * Created by gaowei3 on 2016/12/30.
 */

public class BubbleSort implements ISort {

    /**
     * 冒泡排序，逆序比较，最小的在最上面
     * O(n^2), O(1), 稳定
     */
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();

        int size = array.length;
        boolean need = true;     // 上一轮循环是否交换过，认为交换过
        for (int i = 0; i < size - 1 && need; i++) {       // 循环size-1轮，把size-1个泡泡冒出
            need = false;        // 重置这一轮还没有交换过
            for (int j = size - 1; j >= i + 1; j--) {      // 从下往上比较，比到i，最小的放最上面
                if (array[j - 1] > array[j]) {
                    ArrayUtils.swap(array, j, j - 1);
                    need = true;    // 这一轮交换过了
                }
            }
        }
        return System.currentTimeMillis() - current;
    }
}


