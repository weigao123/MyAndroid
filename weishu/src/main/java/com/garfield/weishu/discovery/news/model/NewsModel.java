package com.garfield.weishu.discovery.news.model;

import com.garfield.weishu.base.OnMyRequestListener;
import com.garfield.weishu.discovery.news.bean.NewsBean;
import com.garfield.weishu.discovery.news.bean.NewsDetailBean;

/**
 * Created by gaowei3 on 2016/11/10.
 */

public interface NewsModel {

    void loadNews(String url, int type, OnMyRequestListener<NewsBean> listener);

    void loadNewsDetail(String docid, OnMyRequestListener<NewsDetailBean> listener);

}
