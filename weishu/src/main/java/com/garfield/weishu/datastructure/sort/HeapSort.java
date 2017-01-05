package com.garfield.weishu.datastructure.sort;

import com.garfield.baselib.utils.string.RandomUtils;

/**
 * Created by gaowei3 on 2017/1/4.
 */

public class HeapSort implements ISort {

    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();
        int size = array.length;
        for (int i = (size - 1) / 2; i >= 0; i --) {
            headAdjust(array, i, size - 1);
        }
        for (int i = size - 1; i > 0; i --) {
            RandomUtils.swap(array, i, 0);
            headAdjust(array, 0, i - 1);
        }
        return System.currentTimeMillis() - current;
    }

    /**
     * 排成大顶堆
     *
     * 第一个for，从下往上排时，每一次都会执行到break，因为下面的已经排好了
     * 第二个for，交换后，从0到last排，可能会导致下面的直接元素不再大顶堆，继续排，但是非直接元素还是大顶堆，不用动
     */
    private void headAdjust(int[] array, int start, int end) {

        // 从最后一个非叶节点开始调整
        // 每一次计算i都是左叶子的索引，++除外
        for (int pos = start * 2 + 1; pos <= end; pos = pos * 2 + 1) {
            if (pos < end && array[pos] < array[pos + 1]) {
                ++pos;
            }
            // 因为下面的都是已经排好序的堆，这样可以保证后面的不需要再比较，保留了以前比较的结果
            if (array[start] >= array[pos]) {
                break;
            }
            RandomUtils.swap(array, start, pos);
            // 下移一代
            start = pos;
        }
    }
}
