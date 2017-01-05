package com.garfield.weishu.datastructure.sort;

/**
 * Created by gaowei3 on 2017/1/5.
 */

public class MergingSort implements ISort {
    @Override
    public long sort(int[] array) {
        return 0;
    }

    /**
     * 把sa[first, middle]和sa[middle+1, last]合入到ta中
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
