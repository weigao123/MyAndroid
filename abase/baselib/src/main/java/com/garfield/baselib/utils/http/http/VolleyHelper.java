package com.garfield.baselib.utils.http.http;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * Created by gaowei3 on 2016/7/4.
 */
public class VolleyHelper {
    private final static String TAG = VolleyHelper.class.getSimpleName();

    private static VolleyHelper mVolleyHelper;
    private UrlBuilder mPath;
    private RequestQueue mQueue;

    public static VolleyHelper getInstance() {
        if (mVolleyHelper == null) {
            synchronized (VolleyHelper.class) {
                mVolleyHelper = new VolleyHelper();
            }
        }
        return mVolleyHelper;
    }
    private VolleyHelper() {
        mQueue = Volley.newRequestQueue(com.garfield.baselib.Cache.getContext());
    }

    public VolleyHelper setParameters(Map<String, String> parameters) {
        mPath.addQueryParameters(parameters);
        return this;
    }

    public RequestQueue addRequest(StringRequest request) {
        if (attachToken()) {
            //mUrlBase.addQueryParameter("access_token", SettingApplication.getAccesToken());
        }
        request.setTag(request.getClass().getSimpleName());
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(request);
        return mQueue;
    }

    protected boolean attachToken() {
        return true;
    }

    public void cancel(String tag) {
        mQueue.cancelAll(tag);
    }


}
