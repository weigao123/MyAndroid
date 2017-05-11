package com.garfield.weishu.nim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;

import com.garfield.baselib.utils.system.L;
import com.garfield.baselib.utils.system.SharedPreferencesUtil;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.app.SettingsPreferences;
import com.garfield.weishu.app.UserPreferences;
import com.garfield.weishu.ui.activity.WelcomeActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

/**
 * Created by gaowei3 on 2016/9/6.
 */
public class NimConfig {

    private static StatusBarNotificationConfig mNotificationConfig;

    public static void initSDK(Context context) {
        NIMClient.init(context, getLoginInfo(), getOptions());
    }

    // 如果返回值为 null，则全部使用默认参数。
    public static SDKOptions getOptions() {

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        mNotificationConfig = new StatusBarNotificationConfig();
        mNotificationConfig.notificationEntrance = WelcomeActivity.class; // 点击通知栏跳转到该Activity
        mNotificationConfig.notificationSmallIconId = R.drawable.ic_launcher;
        // 呼吸灯配置
        mNotificationConfig.ledARGB = Color.GREEN;
        mNotificationConfig.ledOnMs = 1000;
        mNotificationConfig.ledOffMs = 1500;
        // 通知
        mNotificationConfig.vibrate = SettingsPreferences.getVibrateToggle();
        mNotificationConfig.ring = SettingsPreferences.getRingToggle();
        // 通知铃声的uri字符串
        mNotificationConfig.notificationSound = "android.resource://com.garfield.weishu/raw/msg";

        SDKOptions options = new SDKOptions();
        options.statusBarNotificationConfig = mNotificationConfig;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 getOptions 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + AppCache.getContext().getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        //getOptions.thumbnailSize = ${Screen.width} / 2;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public int getDefaultIconResId() {
                return 0;
            }

            @Override
            public Bitmap getTeamIcon(String tid) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return null;
            }
        };
        return options;
    }

    private static LoginInfo getLoginInfo() {
        String account = UserPreferences.getUserAccount();
        String token = UserPreferences.getUserToken();
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            AppCache.setAccount(account);
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }


    public static void nofityWithTopBar() {
        // {MSG_CHATTING_ACCOUNT_NONE} 目前没有与任何人对话，需要状态栏消息通知
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
    }

    public static void nofityWithTopWithout(String account) {
        NIMClient.getService(MsgService.class).setChattingAccount(account, SessionTypeEnum.P2P);
    }

    public static void nofityWithNoTopBar() {
        // {MSG_CHATTING_ACCOUNT_ALL} 目前没有与任何人对话，但能看到消息提醒（比如在消息列表界面），不需要在状态栏做消息通知
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
    }

    public static void toggleNotification(boolean enable) {
        NIMClient.toggleNotification(enable);
    }

    public static void initSetting() {
        toggleNotification(SettingsPreferences.getNotificationToggle());
        AppCache.setHasAnimation(SettingsPreferences.getAnimation());
        AppCache.setNightMode(SharedPreferencesUtil.getBoolean("night_mode"));
    }

    public static void setRingToggle(boolean on) {
        mNotificationConfig.ring = on;
        NIMClient.updateStatusBarNotificationConfig(mNotificationConfig);
    }

    public static void setVibrateToggle(boolean on) {
        mNotificationConfig.vibrate = on;
        NIMClient.updateStatusBarNotificationConfig(mNotificationConfig);
    }
}
