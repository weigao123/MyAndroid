package com.garfield.study.app;

import android.app.Application;
import android.content.Context;

import com.garfield.study.multidex.MultiDex;

/**
 * Created by gaowei on 2017/7/25.
 */

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}