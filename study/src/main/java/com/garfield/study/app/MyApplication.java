package com.garfield.study.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.garfield.baselib.Cache;
import com.garfield.study.multidex.MultiDex;

import java.util.Calendar;

/**
 * Created by gaowei on 2017/7/25.
 */

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Cache.setContext(this);
    }
}
