package com.garfield.weishu.discovery.news.api;

import com.garfield.weishu.discovery.news.bean.zhihu.ZhihuDaily;
import com.garfield.weishu.discovery.news.bean.zhihu.ZhihuStory;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaowei3 on 2016/12/5.
 */

public interface ZhihuApi {

    int NEWS_TYPE_ZHIHU = 8;

    String HOST = "http://news-at.zhihu.com";

    @GET("/api/4/news/latest")
    Observable<ZhihuDaily> getLastDaily();

    @GET("/api/4/news/before/{date}")
    Observable<ZhihuDaily> getTheDaily(@Path("date") String date);

    @GET("/api/4/news/{id}")
    Observable<ZhihuStory> getZhihuStory(@Path("id") String id);
}
