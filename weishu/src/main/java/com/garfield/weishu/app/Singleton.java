package com.garfield.weishu.app;

import com.garfield.baselib.utils.system.L;

/**
 * Created by gaowei on 17/3/4.
 */

public class Singleton {

    private static class SingletonHolder {
        final static Singleton instance = new Singleton();
    }

    private Singleton() {}
    public static Singleton getInstance() {

        return SingletonHolder.instance;
    }


}
