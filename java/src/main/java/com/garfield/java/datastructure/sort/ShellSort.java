package com.garfield.java.datastructure.sort;

/**
 * Created by gaowei on 17/1/2.
 */

public class ShellSort implements ISort {

    /**
     * 希尔排序
     * O(n^1.3), O(1), 不稳定
     */
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();
        int size = array.length;
        // 最后的间隔必须是1
        for (int step = size / 2; step >= 1; step /= 2) {
            // 把距离为step的元素编为一个组，共step组，每一组都要进行直接插入排序
            // 连续的i实际属于不同的组，把所有的组里所有的数字都拿出来，其实就是连续的i
            // 所以是i++，但是都和i-step比较
            for (int i = step; i < size; i++) {
                if (array[i] >= array[i - step])
                    continue;
                int target = array[i];
                int j;
                for (j = i - step; j >= 0 && target < array[j]; j -= step) {
                    array[j + step] = array[j];
                }
                array[j + step] = target;
            }
        }
        return System.currentTimeMillis() - current;
    }

    private void sort2(int[] array) {
        int size = array.length;
        int step = size;
        do {
            step = step / 3 + 1;   // 除以3的只能用do
            for (int i = step; i < size; i++) {
                if (array[i] >= array[i - step])
                    continue;
                int target = array[i];
                int j;
                for (j = i - step; j >= 0 && target < array[j]; j -= step) {
                    array[j + step] = array[j];
                }
                array[j + step] = target;
            }
        } while (step > 1);   // 因为interval加了一个1，所以这里>1
    }
}
