package com.garfield.weishu.developer.test.NonUI;

import com.garfield.baselib.utils.system.L;

/**
 * Created by gaowei on 2017/8/10.
 */

public class HolderBean {

    private static int now = 0;
    private int value;

    public HolderBean() {
        value = now ++;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    private static void reset() {
        L.d("HolderBean Reset");
        now = 0;
    }
}
