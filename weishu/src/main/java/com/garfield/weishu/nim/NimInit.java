package com.garfield.weishu.nim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.garfield.baselib.utils.L;
import com.garfield.weishu.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.config.UserPreferences;
import com.garfield.weishu.http.volley.BaseRequest;
import com.garfield.weishu.http.volley.RegisterRequest;
import com.garfield.weishu.http.volley.VolleyHelper;
import com.google.gson.Gson;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaowei3 on 2016/9/6.
 */
public class NimInit {

    public static void initSDK(Context context) {
        NIMClient.init(context, getLoginInfo(), null);
    }


    // 如果返回值为 null，则全部使用默认参数。
    public static SDKOptions getOptions() {
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

    public static final int REQUEST_SUCCESS = 0;
    public static final int REQUEST_FAILED = 1;

    public interface RequestResult {
        void onResult(int result);
    }

    public interface CancelableRequest {
        void cancel();
    }

    public static CancelableRequest login(String account, String password, final RequestResult callback) {
        final AbortableFuture<LoginInfo> loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, password));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {

            @Override
            public void onSuccess(LoginInfo loginInfo) {
                callback.onResult(REQUEST_SUCCESS);
                Toast.makeText(AppCache.getContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code) {
                L.d("login result : "+code);
                if (code == 302 || code == 404) {
                    callback.onResult(REQUEST_FAILED);
                    Toast.makeText(AppCache.getContext(), R.string.login_account_or_password_wrong, Toast.LENGTH_SHORT).show();
                } else {
                    callback.onResult(REQUEST_FAILED);
                    Toast.makeText(AppCache.getContext(),
                            AppCache.getContext().getResources().getString(R.string.login_failed) + code,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable throwable) {
                callback.onResult(REQUEST_FAILED);
                Toast.makeText(AppCache.getContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
            }
        });
        return new CancelableRequest() {
            @Override
            public void cancel() {
                loginRequest.abort();
            }
        };
    }

    public static CancelableRequest register(final String account, final String nickname, final String password, final RequestResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("accid", account);
        params.put("name", nickname);
        params.put("token", password);
        final RequestQueue queue = VolleyHelper.getInstance()
                .addRequest(new RegisterRequest(params, new BaseRequest.RequestResult<RegisterRequest.RegisterResultBean>() {
                    @Override
                    public void onResult(RegisterRequest.RegisterResultBean result) {
                        if (result != null) {
                            if (result.getCode() == 200) {
                                Toast.makeText(AppCache.getContext(), R.string.register_success, Toast.LENGTH_SHORT).show();
                                callback.onResult(REQUEST_SUCCESS);
                                return;
                            } else if ("already register".equals(result.getDesc())) {
                                Toast.makeText(AppCache.getContext(), R.string.account_registered, Toast.LENGTH_SHORT).show();
                            }
                            callback.onResult(REQUEST_FAILED);
                        }
                    }
                }));

        return new CancelableRequest() {
            @Override
            public void cancel() {
                queue.cancelAll(RegisterRequest.class.getSimpleName());
            }
        };
    }


    public static void logout() {
        AppCache.clear();
    }





























    public static AsyncTask registerAccount(final String account, final String nickname, final String password, final RequestResult callback) {

        class MyAsyncTask extends AsyncTask<Void, Void, Void> {
            private int result;

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    String url = "https://api.netease.im/nimserver/user/create.action";
                    HttpPost httpPost = new HttpPost(url);

                    String appKey = "80ac1ba7e2251f3fa4056e7af4f40b6c";
                    String appSecret = "194f43e90c09";
                    String nonce =  "12345";
                    String curTime = String.valueOf((new Date()).getTime() / 1000L);
                    String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

                    // 设置请求的header
                    httpPost.addHeader("AppKey", appKey);
                    httpPost.addHeader("Nonce", nonce);
                    httpPost.addHeader("CurTime", curTime);
                    httpPost.addHeader("CheckSum", checkSum);
                    httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

                    // 设置请求的参数
                    List<NameValuePair> nvps = new ArrayList<>();
                    nvps.add(new BasicNameValuePair("accid", account));
                    nvps.add(new BasicNameValuePair("name", nickname));
                    nvps.add(new BasicNameValuePair("token", password));
                    httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

                    // 执行请求
                    HttpResponse response = httpClient.execute(httpPost);
                    L.d("register result: " + EntityUtils.toString(response.getEntity(), "utf-8"));
                    if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                        String result=EntityUtils.toString(response.getEntity());
                        System.out.println(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                callback.onResult(result);
            }
        }
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
        return myAsyncTask;
    }
}
