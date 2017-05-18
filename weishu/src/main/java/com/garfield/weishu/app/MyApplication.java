package com.garfield.weishu.app;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.garfield.baselib.utils.http.image.ImageLoaderHelper;
import com.garfield.baselib.utils.system.L;
import com.garfield.baselib.utils.system.SystemUtil;
import com.garfield.weishu.contact.query.PinYin;
import com.garfield.weishu.nim.NimConfig;
import com.garfield.weishu.nim.cache.DataCacheManager;
import com.garfield.weishu.nim.cache.LoginSyncHelper;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;


/**
 * Created by gaowei3 on 2016/8/30.
 */
public class MyApplication extends Application {

    public static final String NIM_BASE_URL = "https://api.netease.im/nimserver/user";

    @Override
    public void onCreate() {
        super.onCreate();

        AppCache.setContext(this);

        NimConfig.initSDK(this);

        /**
         * 多进程，会执行多遍onCreate
         */
        if (SystemUtil.inMainProcess()) {
            L.d("MyApplication");

            Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
            Bugly.init(getApplicationContext(), "cdced5fee1", true);
            LeakCanary.install(this);

            ImageLoaderHelper.init();
            NimConfig.initSetting();

            PinYin.init(this);
            PinYin.validate();

            // 监听SDK数据同步是否完成，同步时应该在主页显示进度条
            LoginSyncHelper.getInstance().registerSDKLoginSyncData();
            // 同步时，SDK的数据更新到缓存
            DataCacheManager.observeSDKDataChanged();
            // 获取SDK数据到缓存
            if (!TextUtils.isEmpty(UserPreferences.getUserAccount()) && !TextUtils.isEmpty(UserPreferences.getUserToken())) {
                DataCacheManager.buildDataCache();
            }

        }
    }

    public class CrashHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            L.d(Log.getStackTraceString(ex));
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }




}
