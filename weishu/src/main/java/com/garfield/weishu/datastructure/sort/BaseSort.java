package com.garfield.weishu.datastructure.sort;

/**
 * Created by gaowei3 on 2016/12/30.
 */

public class BaseSort  {

    static void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }


}
