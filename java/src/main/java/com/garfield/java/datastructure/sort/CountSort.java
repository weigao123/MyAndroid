package com.garfield.java.datastructure.sort;

/**
 * Created by gaowei on 2018/1/14.
 */

public class CountSort implements ISort {

    private static final int max = 99;
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();

        int[] tmp = new int[max + 1];
        // 相当于画正号，index是原始值，value是个数
        for (int value : array) {
            ++tmp[value];
        }
        // 下面共三个索引
        int index = 0;    // 遍历原始array
        for (int i = 0; i < tmp.length; i++) {    // 遍历临时数组
            for (int j = 0; j < tmp[i]; j++) {    // 遍历临时数组中每一位的总数
                array[index++] = i;
            }
        }
        return System.currentTimeMillis() - current;
    }
}
