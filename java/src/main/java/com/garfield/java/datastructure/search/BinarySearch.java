package com.garfield.java.datastructure.search;

/**
 * Created by gaowei3 on 2017/3/15.
 */

/**
 * 二分查找/折半查找，前提是数据有序
 */
public class BinarySearch {

    // 返回位置
    public static int search(int[] array, int a) {
        int left = 0;
        int right = array.length - 1;
        // 每次都只移动一边，因为另外一边是界内，加上left<right保护，所以移动的一边肯定不会出界
        // 最后left和right可以重合，重合后如果不是，仍然会移动指针接着退出循环
        while (left <= right) {
            int mid = (left + right) / 2;
            if (array[mid] == a) {
                return mid;
            } else if (array[mid] < a) {
                left = mid + 1;        //肯定不需要包括自己了，所以排除自己
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    public static int searchRec(int[] array, int a) {
        return searchInner(array, a, 0, array.length - 1);
    }

    private static int searchInner(int[] array, int a, int left, int right) {
        if (left <= right) {
            int mid = (left + right) >> 1;    //位运算
            if (array[mid] == a) {
                return mid;
            } else if (array[mid] > a) {
                return searchInner(array, a, left, mid - 1);   //这里又忘了必须return
            } else {
                return searchInner(array, a, mid + 1, right);
            }
        }
        return -1;
    }


}
