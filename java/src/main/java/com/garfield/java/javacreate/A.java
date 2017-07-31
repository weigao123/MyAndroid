package com.garfield.java.javacreate;

/**
 * Created by gaowei on 2017/7/26.
 */

public class A extends Base {

    private static B b;
    private A a;

    public void setB(B b) {
        this.b = b;
    }

    public void setA(A a) {
        this.a = a;
    }

    public B getB() {
        return b;
    }

    public static void test() {
        new B();
        //L.p("A.Static:  " + B.class.getClassLoader());
    }


}
