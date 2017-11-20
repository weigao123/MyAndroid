package com.garfield.baselib;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Created by gaowei3 on 2016/11/30.
 */

public class Cache {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        if (context instanceof Application) {
            Cache.context = context;
        } else {
            Cache.context = context.getApplicationContext();
        }
    }
}
