package com.garfield.baselib;

import android.content.Context;

/**
 * Created by gaowei3 on 2016/11/30.
 */

public class Cache {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Cache.context = context.getApplicationContext();
    }
}
