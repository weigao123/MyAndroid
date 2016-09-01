package com.garfield.weishu.application;

import android.app.Application;

import com.garfield.weishu.config.AppCache;
import com.garfield.weishu.sdk.nim.NimHelper;
import com.netease.nimlib.sdk.NIMClient;

/**
 * Created by gaowei3 on 2016/8/30.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCache.setContext(this);

        //init以外的所有方法，都只能在UI进程中调用
        NIMClient.init(this, NimHelper.loginInfo(), NimHelper.options(this));


    }
}
