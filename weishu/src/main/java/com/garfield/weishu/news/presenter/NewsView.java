package com.garfield.weishu.news.presenter;

import com.garfield.weishu.news.bean.NewsBean;

import java.util.List;

/**
 * Created by gaowei3 on 2016/11/10.
 */

public interface NewsView<T> {

    void startLoad();
    void dataLoaded(List<T> data);
    void loadFailed();
}
