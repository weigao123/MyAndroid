package com.garfield.weishu.discovery.news.presenter;

/**
 * Created by gaowei3 on 2016/11/10.
 */

public interface NewsPresenter {
    void loadNews(int type, int pageIndex);
    void loadNewsDetail(String docId);
    void cancel();
}
