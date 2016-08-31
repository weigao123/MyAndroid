package com.garfield.weishu.sdk.nim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

import com.garfield.baselib.utils.L;
import com.garfield.weishu.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

/**
 * Created by gaowei3 on 2016/8/30.
 */
public class NimInit {

    // 如果返回值为 null，则全部使用默认参数。
    public static SDKOptions options(Context context) {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        //config.notificationEntrance = WelcomeActivity.class; // 点击通知栏跳转到该Activity
        //config.notificationSmallIconId = R.drawable.ic_stat_notify_msg;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + context.getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        //options.thumbnailSize = ${Screen.width} / 2;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public int getDefaultIconResId() {
                return R.drawable.ic_launcher;
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

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    public static LoginInfo loginInfo() {
        return null;
    }


    /**
     * 登录
     */
    public static void login(String account, String password, final LoginResult loginResult) {
        LoginInfo info = new LoginInfo(account, password);
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo loginInfo) {
                        loginResult.onResult(LOGIN_SUCCESS);
                        L.d("login success");
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 302 || code == 404) {
                            loginResult.onResult(LOGIN_FAILED_A_P_WRONG);
                        } else {
                            loginResult.onResult(LOGIN_FAILED_OTHER_REASON);
                        }
                        L.d("login onFailed: "+code);
                    }
                    @Override
                    public void onException(Throwable throwable) {
                        loginResult.onResult(LOGIN_FAILED_OTHER_REASON);
                    }
                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }

    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_FAILED_A_P_WRONG = 1;
    public static final int LOGIN_FAILED_OTHER_REASON = 2;
    public interface LoginResult {
        void onResult(int result);
    }
}
