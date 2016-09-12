package com.garfield.weishu;

import android.app.Application;
import android.text.TextUtils;

import com.garfield.weishu.config.UserPreferences;
import com.garfield.weishu.nim.DataCacheManager;
import com.garfield.weishu.nim.NimInit;
import com.netease.nimlib.sdk.auth.LoginInfo;


/**
 * Created by gaowei3 on 2016/8/30.
 */
public class MyApplication extends Application {

    public static final String NIM_BASE_URL = "https://api.netease.im/nimserver/user";

    @Override
    public void onCreate() {
        super.onCreate();
        AppCache.setContext(this);

        NimInit.initSDK(this);
        DataCacheManager.observeSDKDataChanged(true);

    }


}
