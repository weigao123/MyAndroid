package com.garfield.weishu.discovery.news.model;

import com.garfield.weishu.base.OnMyRequestListener;
import com.garfield.weishu.discovery.news.bean.netease.NewsBean;
import com.garfield.weishu.discovery.news.bean.netease.NewsDetailBean;
import com.garfield.weishu.discovery.news.bean.zhihu.ZhihuDaily;
import com.garfield.weishu.discovery.news.bean.zhihu.ZhihuDailyItem;
import com.garfield.weishu.discovery.news.bean.zhihu.ZhihuStory;

/**
 * Created by gaowei3 on 2016/11/10.
 */

public interface NewsModel {

    void loadNews(int type, int pageIndex, OnMyRequestListener<NewsBean> listener);

    void loadNewsDetail(String docid, OnMyRequestListener<NewsDetailBean> listener);

    void loadZhihu(String date, OnMyRequestListener<ZhihuDaily> listener);

    void loadZhihuDetail(String docid, OnMyRequestListener<ZhihuStory> listener);

    void cancel();
}
