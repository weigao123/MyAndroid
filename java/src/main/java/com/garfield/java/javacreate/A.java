package com.garfield.java.javacreate;

import com.garfield.java.util.L;

/**
 * Created by gaowei on 2017/7/26.
 */

public class A extends Base {

    private B b;
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

    static {
        //b = new B();
        L.p("A.Static:  " + B.class.getClassLoader());
    }


}
