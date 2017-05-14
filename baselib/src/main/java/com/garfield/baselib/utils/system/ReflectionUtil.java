package com.garfield.baselib.utils.system;

import android.text.TextUtils;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射工具类
 * 通过反射获得对应函数功能
 */
public class ReflectionUtil {

    /**
     * 方法
     */
    public static Method obtainMethod(Class clazz, String methodName, Class[] paramTypes) {
        if (clazz == null || TextUtils.isEmpty(methodName)) {
            return null;
        }
        try {
            Method method = clazz.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeMethod(Object obj, String methodName, Object[] params) {
        Class<?>[] paramTypes = null;
        if (params != null) {
            paramTypes = new Class[params.length];
            for (int i = 0; i < params.length; ++i) {
                paramTypes[i] = params[i].getClass();
            }
        }
        Method method = obtainMethod(obj.getClass(), methodName, paramTypes);
        try {
            return method.invoke(obj, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 属性
     */
    public static Field obtainField(Class clazz, String fieldName) {
        if (clazz == null || TextUtils.isEmpty(fieldName)) {
            return null;
        }
        while (clazz != Object.class) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (Exception ignored) {
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        Field field = obtainField(obj.getClass(), fieldName);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) {
        Field field = obtainField(obj.getClass(), fieldName);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}