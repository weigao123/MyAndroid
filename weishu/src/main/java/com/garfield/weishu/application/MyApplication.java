package com.garfield.weishu.application;

import android.app.Application;

import com.garfield.weishu.netease.NeteaseCloud;
import com.netease.nimlib.sdk.NIMClient;

/**
 * Created by gaowei3 on 2016/8/30.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NIMClient.init(this, NeteaseCloud.loginInfo(), NeteaseCloud.options());
    }
}
