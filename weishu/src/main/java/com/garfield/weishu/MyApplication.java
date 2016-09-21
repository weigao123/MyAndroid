package com.garfield.weishu;

import android.app.Application;
import android.text.TextUtils;

import com.garfield.weishu.config.UserPreferences;
import com.garfield.weishu.contact.query.PinYin;
import com.garfield.weishu.nim.cache.DataCacheManager;
import com.garfield.weishu.nim.cache.LoginSyncDataStatusObserver;
import com.garfield.weishu.nim.InitSDK;
import com.garfield.baselib.utils.SystemUtil;


/**
 * Created by gaowei3 on 2016/8/30.
 */
public class MyApplication extends Application {

    public static final String NIM_BASE_URL = "https://api.netease.im/nimserver/user";

    @Override
    public void onCreate() {
        super.onCreate();
        AppCache.setContext(this);

        InitSDK.initSDK(this);

        if (SystemUtil.inMainProcess(this)) {
            PinYin.init(this);
            PinYin.validate();

            LoginSyncDataStatusObserver.getInstance().registerLoginSyncDataStatus(true);  // 监听登录同步数据完成通知
            DataCacheManager.observeSDKDataChanged(true);
            if (!TextUtils.isEmpty(UserPreferences.getUserAccount()) && !TextUtils.isEmpty(UserPreferences.getUserToken())) {
                DataCacheManager.buildDataCache();
            }
        }
    }


}
