package com.garfield.java.datastructure.sort;

/**
 * Created by gaowei on 17/1/2.
 */

public class ShellSort implements ISort {

    /**
     * 希尔排序
     */
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();
        int size = array.length;
        int interval = size;
        do {
            interval = interval / 3 + 1;
            // 把距离为interval的元素编为一个组，每一组进行直接插入排序，共interval组
            // 连续的i实际属于不同的组，把所有的组里所有的数字都拿出来，其实就是连续的i
            for (int i = interval; i < size; i++) {
                // 下面才是直接插入排序的核心
                if (array[i] >= array[i - interval])
                    continue;
                int target = array[i];
                int j;
                for (j = i - interval; j >= 0 && target < array[j]; j -= interval) {
                    array[j + interval] = array[j];
                }
                array[j + interval] = target;
            }
        } while (interval > 1);   // 因为interval加了一个1，所以这里>1
        return System.currentTimeMillis() - current;
    }
}
