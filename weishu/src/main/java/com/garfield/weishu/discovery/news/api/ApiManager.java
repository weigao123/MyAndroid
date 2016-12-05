package com.garfield.weishu.discovery.news.api;

import com.garfield.weishu.helper.http.retrofit.RetrofitHelper;

/**
 * Created by gaowei3 on 2016/12/5.
 */

public class ApiManager {

    private static NeteaseApi mNeteaseApi;
    private static ZhihuApi mZhihuApi;

    public static NeteaseApi getNeteaseManager() {
        if (mNeteaseApi == null) {
            mNeteaseApi = RetrofitHelper.getInstance(NeteaseApi.HOST, NeteaseApi.class);
        }
        return mNeteaseApi;
    }

    public static ZhihuApi getZhihuManager() {
        if (mZhihuApi == null) {
            mZhihuApi = RetrofitHelper.getInstance(ZhihuApi.HOST, ZhihuApi.class);
        }
        return mZhihuApi;
    }
}
