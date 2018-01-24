package com.garfield.java.datastructure.sort;

import com.garfield.java.datastructure.util.ArrayUtils;

/**
 * Created by gaowei3 on 2017/1/4.
 */

public class HeapSort implements ISort {

    /**
     * 堆排序
     * O(nlogn), O(1), 不稳定
     */
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();
        int size = array.length;

        // 逆序，从最后一个有叶子的结点开始到0分别作为top，end不变
        // 执行完后整个树是大顶堆
        for (int i = size / 2 - 1; i >= 0; i--) {
            headAdjust(array, i, size - 1);      // top变end不变
        }
        // 把最大的值放最后，再次调整大顶堆，每次交换后，只调整一次
        for (int i = size - 1; i > 0; i--) {          // 交换size-1次
            ArrayUtils.swap(array, 0, i);
            headAdjust(array, 0, i - 1);    // top不变end变
        }
        return System.currentTimeMillis() - current;
    }

    // 把以top为顶的堆，排成大顶堆
    // 前提：top的两个孩子堆都已经是大顶堆了
    private void headAdjust(int[] array, int top, int end) {
        // 拿到左孩子，2x+1是左孩子
        for (int i = top * 2 + 1; i <= end; i = i * 2 + 1) {   // pos肯定是左孩子，可以<=
            // 拿到右孩子，比较出来大孩子
            if (i < end && array[i] < array[i + 1]) {   // 可能没有右孩子，不能<=
                ++i;
            }
            // 两个孩子都比父小，不需要任何操作了
            if (array[top] >= array[i]) {
                break;
            }
            ArrayUtils.swap(array, top, i);   // 交换后，影响了这个孩子堆
            top = i;   // top下移到和自己交换的那个孩子上
        }
    }
}
