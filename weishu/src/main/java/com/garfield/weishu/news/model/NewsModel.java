package com.garfield.weishu.news.model;

import com.garfield.weishu.base.OnMyRequestListener;
import com.garfield.weishu.news.bean.NewsBean;
import com.garfield.weishu.news.bean.NewsDetailBean;

/**
 * Created by gaowei3 on 2016/11/10.
 */

public interface NewsModel {

    void loadNews(String url, int type, OnMyRequestListener<NewsBean> listener);

    void loadNewsDetail(String docId, OnMyRequestListener<NewsDetailBean> listener);

}
