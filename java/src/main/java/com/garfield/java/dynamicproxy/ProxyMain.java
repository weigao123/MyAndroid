package com.garfield.java.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by gaowei on 2017/8/3.
 */

public class ProxyMain {
    public static void main() {

        Subject realSubject = new RealSubject();
        InvocationHandler handler = new DynamicProxy(realSubject);
        Subject subject = (Subject) Proxy.newProxyInstance(handler.getClass().getClassLoader(), realSubject
                .getClass().getInterfaces(), handler);
        subject.hello("world");




    }
}
