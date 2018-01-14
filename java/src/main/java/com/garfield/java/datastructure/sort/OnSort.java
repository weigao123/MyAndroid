package com.garfield.java.datastructure.sort;

/**
 * Created by gaowei on 2018/1/14.
 */

public class OnSort implements ISort {

    private static final int max = 99;
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();

        int[] tmp = new int[max + 1];
        for (int value : array) {
            ++tmp[value];
        }
        int index = 0;
        for (int i = 0; i < max + 1; i++) {
            for (int j = 0; j < tmp[i]; j++) {
                array[index++] = i;
            }
        }
        return System.currentTimeMillis() - current;
    }
}
