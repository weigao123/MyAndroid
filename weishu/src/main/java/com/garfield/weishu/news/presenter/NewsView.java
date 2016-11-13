package com.garfield.weishu.news.presenter;

import com.garfield.weishu.news.bean.NewsBean;

import java.util.List;

/**
 * Created by gaowei3 on 2016/11/10.
 */

/**
 * 不是listener的角色，而是用来操作View层界面
 */
public interface NewsView<T> {

    void onLoadBefore();
    void onLoadSuccess(List<T> data);
    void onLoadFailed();
}
