package com.garfield.weishu.app;

import android.app.Application;
import android.text.TextUtils;

import com.garfield.baselib.utils.drawable.ImageLoaderUtils;
import com.garfield.baselib.utils.file.DirectoryUtils;
import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.app.UserPreferences;
import com.garfield.weishu.contact.query.PinYin;
import com.garfield.weishu.nim.cache.DataCacheManager;
import com.garfield.weishu.nim.cache.LoginSyncHelper;
import com.garfield.weishu.nim.NimConfig;
import com.garfield.baselib.utils.system.SystemUtil;
import com.garfield.weishu.setting.SettingFragment;
import com.garfield.weishu.ui.view.ImageLoaderKit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;


/**
 * Created by gaowei3 on 2016/8/30.
 */
public class MyApplication extends Application {

    public static final String NIM_BASE_URL = "https://api.netease.im/nimserver/user";
    public static ImageLoaderKit imageLoaderKit;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCache.setContext(this);
        NimConfig.initSDK(this);

        /**
         * 多进程，会执行多遍onCreate
         */
        if (SystemUtil.inMainProcess(this)) {
            Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());

            ImageLoaderUtils.initImageLoader(this);
            SettingFragment.initSetting();

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
            ex.printStackTrace();

            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            ex.printStackTrace(printWriter);
            L.d("崩溃：\n " + result.toString());

            put(result.toString());
            System.exit(0);
            try {
                throw ex;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }


    public void put(String value) {
        BufferedWriter out = null;
        try {
            File file;
            for (int i = 0; ; i++) {
                file = new File(DirectoryUtils.getOwnCacheDirectory(this, "/crash/"), "bug_" + i + ".txt");
                if (!file.exists()) {
                    break;
                }
            }
            out = new BufferedWriter(new FileWriter(file), 2048);
            out.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
