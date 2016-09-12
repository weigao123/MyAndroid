package com.garfield.weishu.nim;

import android.content.Context;
import android.os.Handler;

import com.garfield.weishu.AppCache;
import com.garfield.weishu.nim.framework.SingleThreadExecutor;
import com.netease.nimlib.sdk.Observer;

import java.util.ArrayList;
import java.util.List;


public class DataCacheManager {

    private static final String TAG = DataCacheManager.class.getSimpleName();

    /**
     * App初始化时向SDK注册数据变更观察者
     */
    public static void observeSDKDataChanged(boolean register) {
        LoginSyncDataStatusObserver.getInstance().registerLoginSyncDataStatus(true);  // 监听登录同步数据完成通知

        FriendDataCache.getInstance().registerObservers(register);
        UserInfoCache.getInstance().registerObservers(register);
        //TeamDataCache.getInstance().registerObservers(register);
    }

    /**
     * 本地缓存构建(异步)
     */
    public static void buildDataCacheAsync() {
        buildDataCacheAsync(null, null);
    }

    /**
     * 本地缓存构建(异步)
     */
    public static void buildDataCacheAsync(final Context context, final Observer<Void> buildCompletedObserver) {
        SingleThreadExecutor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                buildDataCache();

                // callback
                if (context != null && buildCompletedObserver != null) {
                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            buildCompletedObserver.onEvent(null);
                        }
                    });
                }

            }
        });
    }

    /**
     * 本地缓存构建（同步）
     */
    public static void buildDataCache() {
        // clear
        clearDataCache();

        // build user/friend/team data cache
        FriendDataCache.getInstance().buildCache();
        UserInfoCache.getInstance().buildCache();
        //TeamDataCache.getInstance().buildCache();

        // build self avatar cache
        List<String> accounts = new ArrayList<>(1);
        accounts.add(AppCache.getAccount());
        //NimUIKit.getImageLoaderKit().buildAvatarCache(accounts);
    }

    /**
     * 清空缓存（同步）
     */
    public static void clearDataCache() {
        // clear user/friend/team data cache
        FriendDataCache.getInstance().clear();
        UserInfoCache.getInstance().clear();
        //TeamDataCache.getInstance().clear();

        // clear avatar cache
        //NimUIKit.getImageLoaderKit().clear();
    }

    /**
     * 输出缓存数据变更日志
     */
    public static void Log(List<String> accounts, String event, String logTag) {
        StringBuilder sb = new StringBuilder();
        sb.append(event);
        sb.append(" : ");
        for (String account : accounts) {
            sb.append(account);
            sb.append(" ");
        }
        sb.append(", total size=" + accounts.size());

    }
}
