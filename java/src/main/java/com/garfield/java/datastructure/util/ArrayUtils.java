package com.garfield.java.datastructure.util;

import com.garfield.java.util.L;

import java.util.Stack;

/**
 * Created by gaowei on 17/1/1.
 */

public class ArrayUtils {

    public static int[] getOrderlyArray(int sum) {
        int[] result = new int[sum];
        for (int i = 0; i < sum; i++) {
            result[i] = i;
        }
        return result;
    }

    public static int[] getRandomArray(int sum) {
        int[] result = new int[sum];
        for (int i = 0; i < sum; i++) {
            result[i] = i;
        }
        for (int i = 0; i < sum; i++) {
            int random = (int) (sum * Math.random());
            swap(result, i, random);
        }
        return result;
    }

    public static void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(char[] array, int i, int j) {
        char tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static int[][] generateMatrix(int row, int column) {
        int[][] matrix = new int[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                matrix[i][j] = getRandomInt(9);
            }
        }
        printMatrix(matrix);
        return matrix;
    }

    public static int[][] generateIncMatrix() {
        int[][] matrix = new int[][]{{1,2,8,9},{2,4,9,12},{4,7,10,13},{6,8,11,15}};
        printMatrix(matrix);
        return matrix;
    }

    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                L.ds(matrix[i][j]);
            }
            L.dl("");
        }
        L.dl("");
    }

    public static void printStack(Stack<Character> stack) {
        Stack tmp = (Stack) stack.clone();
        Stack<Character> tmp2 = new Stack<>();
        while (!tmp.isEmpty()) {
            tmp2.push((char) tmp.pop());
        }
        while (!tmp2.isEmpty()) {
            L.c(tmp2.pop());
        }
        L.dl("");
    }

    public static int calStack(Stack<Character> stack) {
        Stack tmp = (Stack) stack.clone();
        int sum = 0;
        while (!tmp.isEmpty()) {
            sum += (char) tmp.pop();
        }
        return sum;
    }

    public static int getRandomInt(int max) {
        return (int) (max * Math.random());
    }

}
