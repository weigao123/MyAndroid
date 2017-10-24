package com.garfield.java.datastructure.sort;

import com.garfield.java.datastructure.util.ArrayUtils;

/**
 * Created by gaowei3 on 2017/1/4.
 */

public class HeapSort implements ISort {

    /**
     * 堆排序
     * 最后一行和书不一样
     */
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();
        int size = array.length;

        // 从最后一个有叶子的结点(size-1)/2开始
        // 从下往上排，这样保证了每次headAdjust时，两个叶子为顶点的堆，已经是大顶堆
        // 执行完后整个树是大顶堆
        for (int i = (size - 1) / 2; i >= 0; i --) {
            // 因为每一个结点都不是大顶堆，所以要从小到大依次构建
            headAdjust(array, i, size - 1);
        }

        for (int i = size - 1; i > 0; i --) {
            // 把最大的值放最后去
            ArrayUtils.swap(array, i, 0);
            // 因为第一个结点的两个叶子结点都已经是大顶堆了，所以执行一次构建即可
            headAdjust(array, 0, i - 1);
        }
        return System.currentTimeMillis() - current;
    }

    /**
     * 把以start为顶的堆，排成大顶堆，顶最大
     * 在调用时，需要保证两个叶子为顶点的堆，已经是大顶堆，所以从最后一个非叶子结点开始排序
     */
    private void headAdjust(int[] array, int start, int end) {

        // 每一次循环pos都是左叶子的索引，++除外
        // 交换后，可能会导致下面的叶子元素比下面的叶子小，所以要继续
        for (int pos = start * 2 + 1; pos <= end; pos = pos * 2 + 1) {
            // 找两个叶子里最大的那个，把最大的叶子和顶点交换
            if (pos < end && array[pos] < array[pos + 1]) {
                ++pos;
            }
            // 因为叶子为顶点的堆都是已经排好序的，如果大于大叶子，后面的肯定也不需要再比较了
            // 这个说明利用了以前处理好的结果，性能好
            if (array[start] >= array[pos]) {
                break;
            }
            ArrayUtils.swap(array, start, pos);
            // 下移一代
            start = pos;
        }
    }
}
