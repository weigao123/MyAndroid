package com.garfield.weishu.nim;

import android.content.Intent;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.garfield.baselib.base.BaseActivity;
import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.app.SettingsPreferences;
import com.garfield.weishu.helper.http.volley.BaseRequest;
import com.garfield.weishu.helper.http.volley.RegisterRequest;
import com.garfield.baselib.utils.http.http.VolleyHelper;
import com.garfield.weishu.helper.http.volley.RegisterResultBean;
import com.garfield.weishu.nim.cache.DataCacheManager;
import com.garfield.weishu.nim.cache.LoginSyncHelper;
import com.garfield.weishu.ui.activity.WelcomeActivity;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaowei3 on 2016/9/21.
 */

public class RegisterAndLogin {

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
                L.toast(R.string.login_success);
            }

            @Override
            public void onFailed(int code) {
                L.d("login result : "+code);
                if (code == 302 || code == 404) {
                    callback.onResult(REQUEST_FAILED);
                    L.toast(R.string.login_account_or_password_wrong);
                } else {
                    callback.onResult(REQUEST_FAILED);
                    L.toast(AppCache.getContext().getResources().getString(R.string.login_failed) + code);
                }
            }

            @Override
            public void onException(Throwable throwable) {
                callback.onResult(REQUEST_FAILED);
                L.toast(R.string.login_failed);
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
                .addRequest(new RegisterRequest(params, new BaseRequest.RequestResult<RegisterResultBean>() {
                    @Override
                    public void onResult(RegisterResultBean result) {
                        if (result != null) {
                            if (result.getCode() == 200) {
                                L.toast(R.string.register_success);
                                callback.onResult(REQUEST_SUCCESS);
                                return;
                            } else if ("already register".equals(result.getDesc())) {
                                L.toast(R.string.account_registered);
                            } else {
                                L.toast(AppCache.getContext().getResources().getString(R.string.register_failed, result.getCode()));
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


    public static void logout(BaseActivity context) {
        SettingsPreferences.saveUserToken("");
        AppCache.clear();
        DataCacheManager.clearDataCache();
        LoginSyncHelper.getInstance().reset();
        context.startActivity(new Intent(context, WelcomeActivity.class));
        context.finish();
        context.overridePendingTransition(0, 0);
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
