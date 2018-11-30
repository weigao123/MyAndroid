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
        for (int i = size / 2 - 1; i >= 0; i--) {     // 不是s/2-1就是s/2+1
            headAdjust(array, i, size - 1);      // 每次都到底
        }
        // 把最大的值从最后一位开始放置，再次调整大顶堆，每次交换后，只调整一次
        for (int i = size - 1; i > 0; i--) {          // 交换size-1次
            ArrayUtils.swap(array, 0, i);           // 最后一位依次递减
            headAdjust(array, 0, i - 1);    // top不变end变
        }
        return System.currentTimeMillis() - current;
    }

    // 【大顶堆】
    // 【在top的两个孩子堆都已经是大顶堆的基础上，把top也调整为大顶堆】
    private void headAdjust(int[] array, int top, int end) {
        // i一直是大孩子，top一直是要调整的顶点
        for (int i = top * 2 + 1; i <= end; i = top * 2 + 1) {    // 拿到top的左孩子，i=2*top+1是左孩子
            if (i < end && array[i] < array[i + 1]) {   // 拿到大孩子，i<end才能有右兄弟
                ++i;
            }
            if (array[top] >= array[i]) {     // 两个孩子都比父小，已经是大顶堆了
                break;
            }
            ArrayUtils.swap(array, top, i);   // 只要交换，就可能影响这个孩子的堆
            top = i;   // top下移到和自己交换的那个孩子上
        }
    }
}
