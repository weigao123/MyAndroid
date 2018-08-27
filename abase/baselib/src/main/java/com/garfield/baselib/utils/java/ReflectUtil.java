package com.garfield.baselib.utils.java;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {

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

    /**
     * @param obj 实例方法使用instance，静态方法使用class
     * @param params 无参传入null
     */
    public static Object invokeMethod(Object obj, String methodName, Object[] params) {
        Class<?>[] paramTypes = null;
        if (params != null) {
            paramTypes = new Class[params.length];
            for (int i = 0; i < params.length; ++i) {
                paramTypes[i] = params[i].getClass();
            }
        }

        // 增加判断防止丢失类型
        Method method = obtainMethod(obj instanceof Class ? (Class) obj : obj.getClass(), methodName, paramTypes);
        try {
            return method.invoke(obj instanceof Class ? null : obj, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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
        Field field = obtainField(obj instanceof Class ? (Class) obj : obj.getClass(), fieldName);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) {
        Field field = obtainField(obj instanceof Class ? (Class) obj : obj.getClass(), fieldName);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}