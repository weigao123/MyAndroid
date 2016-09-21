package com.garfield.weishu.nim.cache;

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
    public static void observeSDKDataChanged() {
        FriendDataCache.getInstance().registerSDKObservers(true);
        UserInfoCache.getInstance().registerSDKObservers(true);
        //TeamDataCache.getInstance().registerSDKObservers(true);
    }

    /**
     * 本地缓存构建(异步)
     *
     * 登录完成就开始构建缓存，虽然可能是旧的，但是因为又注册了监听，所以可以保证是新的
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
        clearDataCache();

        FriendDataCache.getInstance().buildCache();
        UserInfoCache.getInstance().buildCache();
        //TeamDataCache.getInstance().buildCache();

        List<String> accounts = new ArrayList<>(1);
        accounts.add(AppCache.getAccount());
        //NimUIKit.getImageLoaderKit().buildAvatarCache(accounts);
    }

    /**
     * 清空缓存（同步）
     */
    public static void clearDataCache() {
        FriendDataCache.getInstance().clear();
        UserInfoCache.getInstance().clear();
        //TeamDataCache.getInstance().clear();

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
