package com.garfield.weishu.discovery.news.api;

import com.garfield.weishu.discovery.news.bean.zhihu.ZhihuDaily;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaowei3 on 2016/12/5.
 */

public interface NeteaseApi {
    //http://blog.csdn.net/modalyin/article/details/51509620

    //新闻列表
    //http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html

    //Head指向的网址
    //http://c.m.163.com/photo/api/set/0001/2212951.json

    int NEWS_TYPE_TOP = 0;
    int NEWS_TYPE_TECHNOLOGY = 1;
    int NEWS_TYPE_ENTERTAINMENT = 2;
    int NEWS_TYPE_FINANCE = 3;
    int NEWS_TYPE_HEALTH = 4;
    int NEWS_TYPE_EMOTION = 5;
    int NEWS_TYPE_TIANJIN = 6;
    int NEWS_TYPE_HOUSE = 7;
    
    int PAGE_SIZE = 20;

    String HOST = "http://c.m.163.com/";

    String HOST_NEWS_TOP = "nc/article/headline/";
    String HOST_NEWS_COMMON = "nc/article/list/";
    String HOST_NEWS_LOCAL = "nc/article/local/";
    String HOST_NEWS_HOUSE = "nc/article/house/";

    String TOP_ID = "T1348647909107";
    String TECHNOLOGY_ID = "T1348649580692";
    String ENTERTAINMENT_ID = "T1348648517839";
    String FINANCE_ID = "T1348648756099";
    String HEALTH_ID = "T1414389941036";
    String EMOTION_ID = "T1348650839000";
    String TIANJIN_ID = "5aSp5rSl";
    String HOUSE_ID = "5aSp5rSl";

    String END_LIST_URL = "-" + PAGE_SIZE + ".html";

    // 新闻详情
    String NEW_DETAIL = "nc/article/";

    String END_DETAIL_URL = "/full.html";

    // 天气预报url
    String WEATHER = "http://wthrcdn.etouch.cn/weather_mini?city=";

    //百度定位
    String INTERFACE_LOCATION = "http://api.map.baidu.com/geocoder";
    
    @GET("{url}")
    Observable<String> getNews(@Path("url") String url);

}
