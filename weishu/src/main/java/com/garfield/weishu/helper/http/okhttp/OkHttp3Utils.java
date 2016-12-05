package com.garfield.weishu.helper.http.okhttp;

import android.os.Handler;
import android.os.Looper;


import com.garfield.baselib.utils.string.JsonUtils;
import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 */
public class OkHttp3Utils {

    private static final String TAG = "OkHttp3Utils";

    private static OkHttp3Utils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private OkHttp3Utils() {
        mOkHttpClient = new OkHttpClient();
        mDelivery = new Handler(Looper.getMainLooper());
    }

    private synchronized static OkHttp3Utils getInstance() {
        if (mInstance == null) {
            mInstance = new OkHttp3Utils();
        }
        return mInstance;
    }

    private void getRequest(String url, final OkHttpResultCallback callback) {
        Request request = new Request.Builder().url(url).build();
        deliveryResult(callback, request);
    }

    private void postRequest(String url, final OkHttpResultCallback callback, List<Param> params) {
        FormBody.Builder formBody = new FormBody.Builder();
        for (Param param : params) {
            formBody.add(param.key, param.value);
        }
        Request request = new Request.Builder().url(url).post(formBody.build()).build();
        deliveryResult(callback, request);
    }

    private void deliveryResult(final OkHttpResultCallback callback, Request request) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailCallback(callback, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String str = response.body().string();
                    if (callback.mType == String.class) {
                        sendSuccessCallBack(callback, str);
                    } else {
                        Object object = JsonUtils.deserialize(str, callback.mType);
                        sendSuccessCallBack(callback, object);
                    }
                } catch (final Exception e) {
                    sendFailCallback(callback, e);
                }
            }
        });

    }

    private void sendFailCallback(final OkHttpResultCallback callback, final Exception e) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFailure(e);
                }
            }
        });
    }

    private void sendSuccessCallBack(final OkHttpResultCallback callback, final Object obj) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onSuccess(obj);
                }
            }
        });
    }


    /**********************对外接口************************/
    public static void get(String url, OkHttpResultCallback callback) {
        getInstance().getRequest(url, callback);
    }

    public static void post(String url, final OkHttpResultCallback callback, List<Param> params) {
        getInstance().postRequest(url, callback, params);
    }

    /**
     * http请求回调类,回调方法在UI线程中执行
     */
    public static abstract class OkHttpResultCallback<T> {

        Type mType;

        public OkHttpResultCallback(){
            mType = getSuperclassTypeParameter(getClass());
        }

        private static Type getSuperclassTypeParameter(Class<?> subclass) {
            /**
             * 获取泛型参数的类型
             */
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onSuccess(T response);

        public abstract void onFailure(Exception e);
    }

    /**
     * post请求参数类
     */
    public static class Param {

        String key;
        String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }


}
