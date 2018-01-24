package com.garfield.weishu.developer.datastructure.sort;

/**
 * Created by gaowei3 on 2017/1/5.
 */

public class MergingSort implements ISort {
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();
        sort(array, 0, array.length - 1);
        return System.currentTimeMillis() - current;
    }

    private void sort(int[] sa, int low, int high) {
//        int mid = (low + high) / 2;
//        if (low < high) {
//            // 左边
//            mergeSort(a, low, mid);
//            // 右边
//            mergeSort(a, mid + 1, high);
//            // 左右归并
//            merge(a, low, mid, high);
//            System.out.println(Arrays.toString(a));
//        }
    }

    /**
     * 把有序的sa[first, middle]和有序的sa[middle+1, last]合并到ta中
     */
    private void merge(int[] sa, int[] ta, int first, int middle, int last) {

        int i, j, k;
        for (i = first, j = middle + 1, k = 0; i <= middle && j <= last; k++) {
            if (sa[i] < sa[j]) {
                ta[k] = sa[i++];
            } else {
                ta[k] = sa[j++];
            }
        }
        if (i <= middle) {
            for (; i <= middle; k++) {
                ta[k] = sa[i++];
            }
        } else if (j <= last) {
            for (; j <= last; k++) {
                ta[k] = sa[j++];
            }
        }
    }
}
