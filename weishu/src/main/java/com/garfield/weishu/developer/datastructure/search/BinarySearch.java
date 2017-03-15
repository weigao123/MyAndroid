package com.garfield.weishu.developer.datastructure.search;

/**
 * Created by gaowei3 on 2017/3/15.
 */

/**
 * 二分查找/折半查找
 */
public class BinarySearch {

    public int search(int[] array, int a) {
        int left = 0;
        int right = array.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (array[mid] == a) {
                return mid;
            } else if (array[mid] < a) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
}
