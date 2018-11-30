package com.garfield.java.multithread;

import com.garfield.java.util.ThreadPoolUtil;

/**
 * Created by gaowei on 2017/6/30.
 */

public class TestMulti {

    public void doTest() {
        final OneToOne oneToOne = new OneToOne();

        ThreadPoolUtil.mExecutors.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    oneToOne.set();
                }
            }
        });
        ThreadPoolUtil.mExecutors.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    oneToOne.get();
                }
            }
        });
    }
}
