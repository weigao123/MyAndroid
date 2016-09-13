package com.garfield.weishu.nim;

/**
 * Created by gaowei3 on 2016/9/13.
 */


public interface MyRequestCallback<T> {
    int MY_REQUEST_SUCCESS = 1;
    int MY_REQUEST_NETWORK_ISSUE = 2;

    void onSuccess(T result);
    void onFailed(int code);
}
