package com.garfield.weishu;

import android.app.Application;
import android.text.TextUtils;

import com.garfield.weishu.config.UserPreferences;
import com.garfield.weishu.contact.query.PinYin;
import com.garfield.weishu.nim.cache.DataCacheManager;
import com.garfield.weishu.nim.cache.LoginSyncData;
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

            // 监听SDK数据同步是否完成，同步时应该在主页显示进度条
            LoginSyncData.getInstance().registerSDKLoginSyncData();
            // 同步时，SDK的数据更新到缓存
            DataCacheManager.observeSDKDataChanged();
            // 获取SDK数据到缓存
            if (!TextUtils.isEmpty(UserPreferences.getUserAccount()) && !TextUtils.isEmpty(UserPreferences.getUserToken())) {
                DataCacheManager.buildDataCache();
            }
        }
    }


}
