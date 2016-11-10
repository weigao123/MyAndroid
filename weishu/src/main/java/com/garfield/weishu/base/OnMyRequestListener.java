package com.garfield.weishu.base;

import java.util.List;

/**
 * Created by gaowei3 on 2016/11/10.
 */

public interface OnMyRequestListener<T> {
    int MY_REQUEST_SUCCESS = 1;
    int MY_REQUEST_NETWORK_ISSUE = 2;

    void onSuccess(List<T> data);
    void onFailure(Exception e);
}
