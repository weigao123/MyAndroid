package com.garfield.weishu.discovery.developer.datastructure.sort;

/**
 * Created by gaowei3 on 2017/1/5.
 */

public class MergingSort implements ISort {
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();
        int size = array.length;
        mSort(array, array, 0, size - 1);
        return System.currentTimeMillis() - current;
    }

    private void mSort(int[] sa, int[] ta, int first, int last) {
//        if (first == last) {
//            ta[first] = sa[first];
//        } else {
//            int[] tmp =
//            int m = (first + last) / 2;
//            mSort(sa, );
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
