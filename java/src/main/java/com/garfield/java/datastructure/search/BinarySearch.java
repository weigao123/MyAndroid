package com.garfield.java.datastructure.search;

/**
 * Created by gaowei3 on 2017/3/15.
 */

/**
 * 二分查找/折半查找，前提是数据有序
 */
public class BinarySearch {

    public int search(int[] array, int a) {
        int left = 0;
        int right = array.length - 1;
        while (left <= right) {             // 最后left和right可能会重合
            int mid = (left + right) / 2;   // 如果是小数，就对应中间偏左的元素
            if (array[mid] == a) {
                return mid;
            } else if (array[mid] < a) {    // 每次只移动其中一个方向
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
}
