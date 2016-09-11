package com.garfield.weishu.http.volley;

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
                RegisterResultBean bean = mGson.fromJson(result, RegisterResultBean.class);
                requestResult.onResult(bean);
            }
        });
    }

    public static class RegisterResultBean {
        private String desc;
        private int code;
        private Info info;

        public Info getInfo() {
            return info;
        }

        public void setInfo(Info info) {
            this.info = info;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }


        private class Info {
            String token;
            String accid;
            String name;

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getAccid() {
                return accid;
            }

            public void setAccid(String accid) {
                this.accid = accid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

}