package com.garfield.java.dynamicproxy;

/**
 * Created by gaowei on 2017/8/3.
 */

public class RealSubject extends ProxyMain implements Subject, Runnable {
    @Override
    public void hello(String str) {
        System.out.println("hello: " + str);
    }

    @Override
    public void run() {

    }
}
