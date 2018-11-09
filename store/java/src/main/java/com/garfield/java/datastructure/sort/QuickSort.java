package com.garfield.java.datastructure.sort;


import com.garfield.java.datastructure.util.ArrayUtils;

/**
 * Created by gaowei3 on 2017/1/3.
 */

public class QuickSort implements ISort {

    /**
     * 快速排序
     * O(nlogn), O(logn), 不稳定
     */
    @Override
    public long sort(int[] array) {
        long current = System.currentTimeMillis();
        qSort(array, 0, array.length - 1);
        return System.currentTimeMillis() - current;
    }

    /**
     * 完整的二叉树层次，类似【前序】，最上面一层先排序
     * 为什么不能用while？
     */
    private void qSort(int[] array, int left, int right) {
        // 递归终止条件
        if (left < right) {
            int pivot = part(array, left, right);
            qSort(array, left, pivot - 1);   // 新建栈帧
            qSort(array, pivot + 1, right);
        }
    }

    /**
     * 以基准为中心，使得左边都是小于的，右边都是大于的
     * 方法执行时已经保证了 left < right
     * 【返回基准所在的位置】
     *
     * 双向扫描版本
     * 两个指针，一左一右，基准来回跳动，一直跳在i和j之间并慢慢缩小范围
     * <不变的指针一直指着基准> 肯定能到相邻，然后要么指针移动变为重合，要么先交换再移动变为重合
     * <最终肯定是重合> i==j是所有循环的退出条件
     */
    public static int part(int[] array, int left, int right) {
        int base = array[right];   //最右作为基准，所以从左开始移动
        int i = left, j = right;
        // 跳出循环，最后肯定会i==j，如果<=就死循环了
        // 完全可以使用i!=j
        while (i < j) {
            // 这是左移动的结束标记
            // 只能i<j，如果一开始就是分好了的就会一直移动，当i==j时会造成下面的++i出界
            // 总结：循环里直接++或--的，判断条件都不能带上=，while的条件类似for(;i<n;i++)中间的条件
            while (i < j && array[i] <= base) {
                ++i;
            }
            ArrayUtils.swap(array, i, j);   //i是>base的那个
            // 一旦i==j相等了，另外一边也就不再执行了
            while (i < j && array[j] >= base) {
                --j;
            }
            ArrayUtils.swap(array, i, j);
        }
        return i;    //忘了，返回的是位置，不是值
    }

    private int part2(int[] array, int left, int right) {
        int base = array[right];   //这个位置设置为空位，空位会一直代表基准的位置
        int i = left, j = right;   //j是空位

        // 这是左右移动的结束标记，最后肯定会i==j
        while (i < j) {
            // 这是左移动的结束标记，i<j防止这个while越界
            while (i < j && array[i] <= base) {
                ++i;
            }
            array[j] = array[i];   //把i放到空位上，i作为新空位
            while (i < j && array[j] >= base) {   //右移动
                --j;
            }
            array[i] = array[j];   //把j再放空位上，j作为新的空位
        }
        // 把基准放到空位上，最后肯定i==j
        array[i] = base;
        return i;
    }



    /**
     * 单向扫描版本
     * 两个指针，都指向左边，基准位置一直不变
     * [left,i-1]都是小于基准，[i,j]都大于等于基准，[j+1,right-1]还未划分
     *
     * j位置发现比基准小时，就拿j和最左边的大于基准的i和j交换
     * 结果就是i和左边的仍然是小于基准，j变成了大于基准
     */
    private int partOneWay(int[] array, int left, int right) {
        int base = array[right];   // 确定基准位置，不一定是最后一个位置
        // i一直指向下一个应该小于基准的位置，也是最左边第一个大于基准的位置
        // j是用来遍历
        int i = left, j = left;   // 同时起步
        while (j < right) {
            // 在前进的路上发现了小于基准的，就扔到后面的篮子里
            if (array[j] < base) {
                if (i != j) {   // 一开始i==j，有可能全都小于所以一直i==j
                    ArrayUtils.swap(array, i, j);
                }
                ++i;   // 在前进的路上发现了小于基准的，i就要跟着移动
            }
            ++j;    // j要一直不停的走
        }
        ArrayUtils.swap(array, i, right);
        return i;
    }
}
