package com.garfield.weishu.nim.cache;

import android.os.Handler;

import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.app.AppCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.constant.LoginSyncStatus;

import java.util.ArrayList;
import java.util.List;


/**
 * 在 Application 的 onCreate 中注册 XXXServiceObserver 来监听数据变化
 * 那么在同步过程中，APP 会收到数据更新通知，此时直接更新缓存。
 * 当同步完成时，缓存也就构建完成了。
 */
public class LoginSyncHelper {

    private static final String TAG = LoginSyncHelper.class.getSimpleName();

    private static final int TIME_OUT_SECONDS = 10;

    private Handler uiHandler;

    private Runnable timeoutRunnable;

    private LoginSyncStatus mSyncStatus = LoginSyncStatus.NO_BEGIN;

    private List<Observer<Void>> observers = new ArrayList<>();

    /**
     * 注销时清除状态&监听
     */
    public void reset() {
        mSyncStatus = LoginSyncStatus.NO_BEGIN;
        observers.clear();
    }

    /**
     * 目的是从服务器同步SDK的数据到最新，比如其他用户改名字了会收到
     *
     * 在App启动时向SDK注册登录后同步数据过程状态的通知
     * 调用时机：主进程Application onCreate中
     *
     * 实际上只有登录账号后才开始的同步
     */
    public void registerSDKLoginSyncData() {
        NIMClient.getService(AuthServiceObserver.class).observeLoginSyncDataStatus(loginSyncStatusObserver, true);
    }

    private Observer<LoginSyncStatus> loginSyncStatusObserver = new Observer<LoginSyncStatus>() {
        @Override
        public void onEvent(LoginSyncStatus status) {
            // 更新现在的状态
            mSyncStatus = status;
            if (status == LoginSyncStatus.BEGIN_SYNC) {
                //L.d(TAG, "login sync begin");
            } else if (status == LoginSyncStatus.SYNC_COMPLETED) {
                //L.d(TAG, "login sync completed");
                onLoginSyncDataCompleted();
            }
        }
    };

    /**
     * 监听登录后同步数据完成事件，缓存构建完成后自动取消监听
     * 调用时机：进入主页后，同步完成后关闭正在同步进度条
     *
     * @return 返回true表示数据同步已经完成或者不进行同步，返回false表示正在同步数据
     */
    public boolean observeSyncDataCompletedEvent(Observer<Void> observer) {
        if (mSyncStatus == LoginSyncStatus.NO_BEGIN || mSyncStatus == LoginSyncStatus.SYNC_COMPLETED) {
            // 正常手动登录后会立刻开始同步，如果未同步说明是已经自动登录了
            return true;
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
        // 超时定时器
        if (uiHandler == null) {
            uiHandler = new Handler(AppCache.getContext().getMainLooper());
        }
        if (timeoutRunnable == null) {
            timeoutRunnable = new Runnable() {
                @Override
                public void run() {
                    // 如果超时还处于开始同步的状态，手动置状态结束
                    if (mSyncStatus == LoginSyncStatus.BEGIN_SYNC) {
                        onLoginSyncDataCompleted();
                    }
                }
            };
        }
        uiHandler.removeCallbacks(timeoutRunnable);
        uiHandler.postDelayed(timeoutRunnable, TIME_OUT_SECONDS * 1000);
        return false;
    }

    /**
     * 登录同步数据完成处理
     */
    private void onLoginSyncDataCompleted() {
        // 移除超时任务（有可能完成包到来的时候，超时任务都还没创建）
        if (timeoutRunnable != null) {
            uiHandler.removeCallbacks(timeoutRunnable);
        }
        // 通知上层
        for (Observer<Void> o : observers) {
            o.onEvent(null);
        }
        reset();
    }

    /**
     * 单例
     */
    public static LoginSyncHelper getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        final static LoginSyncHelper instance = new LoginSyncHelper();
    }
}
