package com.garfield.weishu.datastructure.sort;

/**
 * Created by gaowei3 on 2016/12/30.
 */

public class BubbleSort extends BaseSort {

    int score[] = {67, 69, 75, 87, 89, 90, 99, 100};

    public static int[] bubbleSort(int[] array) {
        int size = array.length;

        // 循环size-1次
        for (int i = 0; i < size - 1; i ++) {
            // 两个元素中的前一个，每次都从0开始遍历，第一次到size-2位置(跟i相关，所以是size-2-i)
            for (int j = 0; j < size - 1 - i; j++) {
                swap(array, j, j+1);
            }
        }
        return array;
    }
}
