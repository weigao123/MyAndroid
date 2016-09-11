package com.garfield.weishu.http.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.garfield.weishu.MyApplication;
import com.garfield.weishu.nim.CheckSumBuilder;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaowei3 on 2016/9/11.
 */
public abstract class BaseRequest<T> extends StringRequest {
    protected static String BASE_URL = MyApplication.NIM_BASE_URL;
    private Map<String, String> mParams;
    private String curTime;
    private String checkSum;
    protected static Gson mGson = new Gson();

    public BaseRequest(String url, Map<String, String> params, final BaseRequestResult baseRequestResult) {
        super(Request.Method.POST, url, new Response.Listener<String> () {
            @Override
            public void onResponse(String response) {
                baseRequestResult.onResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                baseRequestResult.onResult(null);
            }
        });
        mParams = params;
        curTime = String.valueOf((new Date()).getTime() / 1000L);
        checkSum = CheckSumBuilder.getCheckSum("194f43e90c09", "12345", curTime);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("AppKey", "80ac1ba7e2251f3fa4056e7af4f40b6c");
        headers.put("Nonce", "12345");
        headers.put("CurTime", curTime);
        headers.put("CheckSum", checkSum);
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    //public abstract String getPath();
    //public abstract Map<String, String> getCustomParams();

    protected interface BaseRequestResult {
        void onResult(String result);
    }
    public interface RequestResult<T> {
        void onResult(T result);
    }

}
