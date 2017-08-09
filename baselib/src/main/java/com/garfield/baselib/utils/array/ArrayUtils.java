package com.garfield.baselib.utils.array;

import com.garfield.baselib.utils.java.ReflectUtil;

import java.lang.reflect.Array;

/**
 * Created by gaowei on 17/1/1.
 */

public class ArrayUtils {

    public static int[] getArray(int sum) {
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

    @SuppressWarnings("all")
    public static <T> T[] create(Class<T> type, int sum) {
        // 这个方法只会创建一个空数组，内部没有初始化
        T[] result = (T[]) Array.newInstance(type, sum);
        // 元素初始化
        for (int i = 0; i < sum; i ++) {
            try {
                result[i] = type.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        ReflectUtil.invokeMethod(type, "reset", null);
        return result;
    }


//    public static <T> T getTypeArray(Class<T> type, int sum) {
//
//        try {
//            return type.newInstance();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }











//    public static Class[] arraySubtract(Class[] arr, int index){
//        ArrayList<Class> list = new ArrayList<Class>();
//        for (int i = 0; i < arr.length; i++) {
//            if (i != index) {
//                list.add(arr[i]);
//            }
//        }
//        Class[] result = new Class[]{};
//        return list.toArray(result);
//    }
//
//    public static Integer[] arraySubtract(Integer[] arr, int index){
//        ArrayList<Integer> list = new ArrayList<Integer>();
//        for (int i = 0; i < arr.length; i++) {
//            if (i != index) {
//                list.add(arr[i]);
//            }
//        }
//        Integer[] result = new Integer[]{};
//        return list.toArray(result);
//    }
}
