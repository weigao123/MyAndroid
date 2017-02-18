package com.garfield.weishu.helper.http.volley;

import com.garfield.baselib.utils.system.L;

import java.util.Map;

/**
 * Created by gaowei3 on 2016/9/11.
 */
public class RegisterRequest extends BaseRequest {

    private static final String url = BASE_URL + "/create.action";

    public RegisterRequest(Map<String, String> params, final RequestResult<RegisterResultBean> requestResult) {
        super(url, params, new BaseRequestResult() {
            @Override
            public void onResult(String result) {
                L.d("RegisterRequest：" + result);
                RegisterResultBean bean = mGson.fromJson(result, RegisterResultBean.class);
                requestResult.onResult(bean);
            }
        });
    }



}