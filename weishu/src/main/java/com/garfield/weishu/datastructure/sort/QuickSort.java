package com.garfield.weishu.datastructure.sort;

import com.garfield.baselib.utils.string.RandomUtils;

/**
 * Created by gaowei3 on 2017/1/3.
 */

public class QuickSort implements ISort {

    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();
        qSort(array, 0, array.length - 1);
        return System.currentTimeMillis() - current;
    }

    private void qSort(int[] array, int left, int right) {
        int pivot;
        if (left < right) {
            pivot = partition(array, left, right);
            qSort(array, left, pivot - 1);
            qSort(array, pivot + 1, right);
        }
    }

    /**
     * 以枢纽为中心，结果，左边都是小于的，右边都是大于的
     * 返回枢纽所在的位置
     */
    private int partition(int[] array, int left, int right) {
        int pivot = array[left];
        while (left < right) {
            // left和right其中一个指向着pivot的位置，swap后切换
            while (left < right && array[right] >= pivot) {
                --right;
            }
            RandomUtils.swap(array, left, right);
            while (left < right && array[left] <= pivot) {
                ++left;
            }
            RandomUtils.swap(array, left, right);
        }
        return left;
    }
}
