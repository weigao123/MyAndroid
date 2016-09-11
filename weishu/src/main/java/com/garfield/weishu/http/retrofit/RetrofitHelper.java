
package com.garfield.weishu.http.retrofit;

import retrofit2.Retrofit;


public class RetrofitHelper {

    public static final String BASE_URL = "https://api.netease.im/nimserver/user/";

    private static volatile RetrofitHelper mRetrofitHelper = null;
    private HttpService mHttpService;

    public static RetrofitHelper getInstance() {
        if (mRetrofitHelper == null) {
            synchronized (RetrofitHelper.class) {
                mRetrofitHelper = new RetrofitHelper();
            }
        }
        return mRetrofitHelper;
    }

    private RetrofitHelper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
        mHttpService = retrofit.create(HttpService.class);
    }

    public HttpService getHttpService() {
        return mHttpService;
    }

}
