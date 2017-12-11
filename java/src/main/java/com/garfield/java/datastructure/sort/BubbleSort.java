package com.garfield.java.datastructure.sort;


import com.garfield.java.datastructure.util.ArrayUtils;

/**
 * Created by gaowei3 on 2016/12/30.
 */

public class BubbleSort implements ISort {

    /**
     * 冒泡排序
     */
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();
        int size = array.length;

        // 如果某一轮没有交换任何数据，后面就不需要了，省去无效的比较大小
        // 这一轮是否交换了数据
        boolean isSorted = true;
        // 循环size-1次
        for (int i = 0; i < size - 1 && isSorted; i++) {
            isSorted = false;
            // 两个元素中的前一个，每次都从0开始遍历，第一次到size-2位置(跟i相关，所以是size-2-i)
            // 相邻的两个元素两两比较，把最大的沉淀到最下面
            for (int j = 0; j < size - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    ArrayUtils.swap(array, j, j + 1);
                    isSorted = true;
                }
            }
        }
        return System.currentTimeMillis() - current;
    }
}
