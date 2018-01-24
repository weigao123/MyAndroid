package com.garfield.java.datastructure.sort;

/**
 * Created by gaowei3 on 2017/1/5.
 */

public class MergeSort implements ISort {

    /**
     * 归并排序
     * O(nlogn), O(n), 稳定
     */
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();
        sort(array, 0, array.length - 1);
        return System.currentTimeMillis() - current;
    }

    /**
     * 分治思想
     * 完整的二叉树层次，类似【后序】，最底下一层先合并
     */
    private void sort(int[] a, int low, int high) {
        // 左右对半均分，有可能不等长
        if (low < high) {
            int mid = (low + high) / 2;  //mid属于前半段
            sort(a, low, mid);           //low=mid，即只有一个元素时终止
            sort(a, mid + 1, high);
            merge(a, low, mid, high);
        }
    }

    // mid属于左边
    private static void merge(int[] a, int low, int mid, int high) {
        int[] tmp = new int[high - low + 1];
        int i = low;
        int j = mid + 1;
        int k = 0;
        while (i <= mid && j <= high) {
            if (a[i] < a[j]) {
                tmp[k++] = a[i++];
            } else {
                tmp[k++] = a[j++];
            }
        }
        while (i <= mid) {
            tmp[k++] = a[i++];
        }
        while (j <= high) {
            tmp[k++] = a[j++];
        }
        for (int k2 = 0; k2 < tmp.length; k2++) {
            a[k2 + low] = tmp[k2];
        }
    }

}
