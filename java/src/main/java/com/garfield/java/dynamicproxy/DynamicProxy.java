package com.garfield.java.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by gaowei on 2017/8/3.
 */

public class DynamicProxy implements InvocationHandler {

    private Object subject;

    public DynamicProxy(Object subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Method:" + method);

        method.invoke(subject, args);

        return null;
    }
}
