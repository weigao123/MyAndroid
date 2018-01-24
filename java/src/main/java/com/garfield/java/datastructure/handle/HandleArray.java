package com.garfield.java.datastructure.handle;

import com.garfield.java.datastructure.sort.QuickSort;
import com.garfield.java.datastructure.util.ArrayUtils;
import com.garfield.java.util.L;

/**
 * Created by gaowei on 2018/1/18.
 */

public class HandleArray {

    public static void test() {


        //斐波那契数列
        //L.d(frogJump(8));
        //L.d(frogJumpRec(8));

        //汉诺塔
        //hanoiMove(3, 'A', 'B', 'C');

        //矩阵查找值
        //find(ArrayUtils.generateIncMatrix(), 11);

        //顺时针打印矩阵
        //printClockWise(ArrayUtils.generateIncMatrix());

        //循环数组
        //L.d(find(new int[]{1,3,5,6,8,9}, 7));
        //L.d(findCircleMin(new int[]{6,7,9,4,5}));
        //L.d(findCircleMin(new int[]{1,0,1,1,1}));

        //大数问题
        //printToNBit(5);

        //奇偶数顺序
        //L.d(adjustOrder(new int[]{3,8,2,6,1,5}));

        //个数大于一半的数字
        //findCenterNumBM(new int[]{1,8,9,8,1,8,8});

        //查找有序中的某个数字个数
        //findCountInOrder(new int[]{1,2,3,5,5,5,5,5,6}, 5);

    }
    /**——————————————————————————————字符串有序全排列———————————————————————————————————*/
    // 实现思想：将整组数中的所有的数分别与第一个数交换，这样就总是在处理后n-1个数的全排列
    // https://www.cnblogs.com/zyoung/p/6764371.html
    private static void permutation(char[] str, int i) {
        if (i == str.length - 1) {      // 只剩下一个元素，就可以打印了
            L.d(String.valueOf(str));
        } else {
            // 还有多个元素待排列，递归产生排列
            // 第一个元素和后面每一个进行交换，然后递归
            for (int j = i; j < str.length; j++) {
                ArrayUtils.swap(str, i, j);  // 从固定的数后第一个依次交换
                permutation(str, i + 1);
                ArrayUtils.swap(str, i, j);  // 这组递归完成之后需要交换回来
            }
        }
    }
    /**——————————————————————————————字符串无序全组合———————————————————————————————————*/


    /**——————————————————————————————汉诺塔———————————————————————————————————————————*/
    // n个板，从A借助B移动到C上
    private static void hanoiMove(int n, char A, char B, char C) {
        if (n == 1) {
            L.d(A + "->" + C);
            return;
        }
        hanoiMove(n - 1, A, C, B);
        hanoiMove(1, A, B, C);
        hanoiMove(n - 1, B, A, C);
    }
    /**——————————————————————————————类斐波那契数列—————————————————————————————————————*/
    private static int frogJumpRec(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        if (n == 2) {   //青蛙跳才有
            return 2;
        }
        return frogJumpRec(n - 1) + frogJumpRec(n - 2);
    }
    private static int frogJump(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        int prepre = 1;
        int pre = 2;
        int current = 0;
        for (int i = 3; i <= n; i++) {
            current = prepre + pre;
            prepre = pre;  //右移
            pre = current;
        }
        return current;
    }

    /**——————————————————————————————矩阵查找值——————————————————————————————————————*/
    private static int[][] exchange(int[][] matrix) {
        if (matrix != null && matrix.length > 0 && matrix.length == matrix[0].length) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    if (i >= j) continue;   //必须有，否则又换回去了
                    int tmp = matrix[i][j];
                    matrix[i][j] = matrix[j][i];
                    matrix[j][i] = tmp;
                }
            }
        }
        ArrayUtils.printMatrix(matrix);
        return matrix;
    }
    private static boolean find(int[][] matrix, int target) {
        if (matrix == null)
            return false;
        int i = 0;
        int j = matrix[0].length - 1;
        while (i < matrix.length && j >= 0) {
            if (matrix[i][j] == target) {
                L.d("i:"+i+" j:"+j);
                return true;
            } else if (matrix[i][j] > target) {
                --j;
            } else {
                ++i;
            }
        }
        return false;
    }
    /**——————————————————————————————顺时针打印矩阵—————————————————————————————————————*/
    private static void printClockWise(int[][] matrix) {
        int row = matrix.length;
        int column = matrix[0].length;
        int circle = 0;
        boolean done;
        while (true) {
            done = false;
            for (int i = circle; i < column - circle; i++) {
                done = true;
                L.d1(matrix[circle][i]);
            }
            if (!done) return;

            done = false;
            for (int i = circle + 1; i < row - circle; i++) {
                done = true;
                L.d1(matrix[i][column - circle - 1]);
            }
            if (!done) return;

            done = false;
            for (int i = column - circle - 2; i >= circle; i--) {
                done = true;
                L.d1(matrix[row - circle - 1][i]);
            }
            if (!done) return;

            done = false;
            for (int i = row - circle - 2; i > circle; i--) {
                done = true;
                L.d1(matrix[i][circle]);
            }
            if (!done) return;

            ++ circle;
            L.d("");
        }
    }


    /**——————————————————————————————旋转数组的最小数———————————————————————————————————*/
    // 普通二分查找
    private static boolean find(int[] array, int t) {
        if (array == null) return false;
        int i = 0, j = array.length - 1;
        while (i <= j) {
            int mid = (i + j) / 2;
            if (array[mid] == t) {
                return true;
            } else if (array[mid] > t) {
                j = mid - 1;
            } else {
                i = mid + 1;
            }
        }
        return false;
    }
    // 345612 10111
    // 性质：第二个递增数组的最大值(最后一个数)<=第一个递增数组的最小值(第一个数)
    // 要始终保持两个指针一个在第一个递增数组，一个在第二个递增数组，只要不对mid进行+-1
    private static int findCircleMin(int[] array) {
        if (array == null || array.length < 1) return -1;
        int part = array[array.length - 1];
        int i = 0, j = array.length - 1;

        // 如果<肯定是纯递增，而=有可能不是递增
        if (array[i] < array[j]) return array[i];

        while (i <= j) {
            if (j - i == 1) {
                return array[j];
            }
            int mid = (i + j) / 2;

            // 在指针移动的过程中，可能突发，无法判断mid在哪个递增数组中，只能顺序查找
            if (array[i] == array[mid] && array[j] == array[mid]) {
                return findMinSeq(array, i, j);
            }

            // 可以判断mid在哪一个递增数组才行
            if (array[mid] >= part) {
                i = mid;     //缩小左范围，但是绝对不能+1，否则i可能进入第二个递增数组
            } else if (array[mid] <= part) {
                j = mid;
            }
        }
        return -1;
    }
    private static int findMinSeq(int[] array, int left, int right) {
        int result = array[left];
        for (int i = left + 1; i <= right; i++) {
            if (array[i] < result) {
                result = array[i];
                break;
            }
        }
        return result;
    }
    /**——————————————————————————————大数问题————————————————————————————————————————*/
    private static void printToNBit(int n) {
        char[] num = new char[n];
        for (int i = 0; i < n; i++) {
            num[i] = '0';
        }
        while (increase(num)) {
            boolean start = false;
            for (int i = 0; i < n; i++) {
                if (!start && num[i] > '0') {
                    start = true;
                }
                if (start) {
                    System.out.print(num[i]);
                }
            }
            System.out.println();
        }
    }
    private static boolean increase(char[] num) {
        int n = num.length;
        int carry = 1;
        for (int i = n - 1; i >= 0; i--) {
            char next = (char) (num[i] + carry);
            if (i == 0 && next > '9') {
                return false;
            }
            if (next > '9') {
                num[i] = '0';
            } else {
                num[i] = next;
                break;
            }
        }
        return true;
    }
    /**——————————————————————————————奇数前，偶数后—————————————————————————————————————*/
    // 使用分类算法，快速排序的分段法
    // 最后肯定紧贴，想象然后，要么指针移动变为重合，要么先交换再移动变为重合
    // <最终肯定是重合>
    private static int[] adjustOrder(int[] array) {
        if (array == null) return null;
        int i = 0, j = array.length - 1;
        // 可以用i!=j
        while (i < j) {
            while (i < j && (array[i] & 1) == 1) {
                ++i;
            }
            while (i < j && (array[j] & 1) == 0) {
                --j;
            }
            ArrayUtils.swap(array, i, j);
            // if (i == j) L.d("最后肯定重合");
        }
        return array;
    }
    /**——————————————————————————————已知入栈顺序，判断是否为出栈顺序————————————————————*/



    /**——————————————————————————————连续子数组的最大和—————————————————————————————————*/
    // 技巧性处理
    public static int getGreatestSumOfSub(int[] array) {
        int currentSum = 0;              //当前连续n项的和
        int maxSum = Integer.MIN_VALUE;  //连续子元素和的最大值，有可能全是负数
        for (int i = 0; i < array.length; i++) {
            currentSum += array[i];
            if (currentSum > maxSum) {
                maxSum = currentSum;
            }
            if (currentSum < 0) {
                currentSum = 0;
            }
        }
        return maxSum;
    }
    /**——————————————————————————————数组中出现次数超过一半的数字————————————————————————*/
    // 多数投票问题
    // 其实就是相当于找到排序后中间的数，偶数个就是中间的下一个
    // -> 修改原数组
    // 方法1：普通排序，O(logn)
    // 方法2：改进快速排序，O(n)
    // -> 不修改原数组
    // 方法3：散列表，O(n)+O(n)
    // 方法4：BM算法，O(n)
    private static int findCenterNum(int[] array) {         // 快排
        if (array == null || array.length == 0) return -1;
        int i = 0, j = array.length - 1;
        int mid = array.length >> 1;       //因为最后要超过一半
        while (i <= j) {
            int pivot = QuickSort.part(array, i, j);
            if (pivot == mid) {
                break;
            } else if (pivot < mid) {
                i = pivot + 1;
            } else {
                j = pivot - 1;
            }
        }
        L.d(array[mid]);
        return array[mid];
    }
    // BM多数投票算法
    // https://segmentfault.com/a/1190000004905350
    private static int findCenterNumBM(int[] array) {
        if (array == null || array.length == 0) return -1;
        int candidate = -1;
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (count == 0) {
                candidate = array[i];
                count = 1;
            } else if (array[i] == candidate) {
                ++ count;
            } else {
                -- count;
            }
        }
        // BM算法必须要检查，如果没有大于一半的数这个结果就没有意义
        int result = checkHalfNum(array, candidate) ? candidate : -1;
        L.d("candidate:"+candidate+"  half:"+result);
        return result;
    }
    private static boolean checkHalfNum(int[] array, int candidate) {
        int num = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == candidate) {
                ++num;
            }
        }
        return (num << 1) > array.length;
    }

    /**——————————————————————————————数组最小的k个数——————————————————————————————————*/
    // 方法1：改进快速排序，O(n)
    // 方法2：空间换时间，创建数组，适合海量数据

    /**——————————————————————————————数字在有序数组中出现的个数—————————————————————————*/
    // 方法1：普通二分查找，再加普通查找，O(n)
    // 方法2：改进二分查找，先要找到起始位置和结束位置，O(nlogn)
    private static int findCountInOrder(int[] array, int t) {
        if (array == null) return 0;
        int start = findStart(array, t);
        int end = findEnd(array, t);
        int count = 0;
        if (start > -1 && end > -1) {
            count = end - start + 1;
        }
        L.d("count: "+count);
        return count;
    }
    // 返回第一个k的位置
    private static int findStart(int[] array, int t) {
        int i = 0, j = array.length - 1;
        while (i <= j) {
            int mid = (i + j) >> 1;
            if (array[mid] == t) {
                if (mid == 0 || array[mid - 1] < t) {
                    L.d("start:" + mid);
                    return mid;
                } else {
                    j = mid - 1;
                }
            } else if (array[mid] > t) {
                j = mid - 1;
            } else {
                i = mid + 1;
            }
        }
        return -1;
    }
    private static int findEnd(int[] array, int t) {
        int i = 0, j = array.length - 1;
        while (i <= j) {
            int mid = (i + j) >> 1;
            if (array[mid] == t) {
                if (mid == array.length - 1 || array[mid + 1] > t) {
                    L.d("end:" + mid);
                    return mid;
                } else {
                    i = mid + 1;
                }
            } else if (array[mid] > t) {
                j = mid - 1;
            } else {
                i = mid + 1;
            }
        }
        return -1;
    }
}
