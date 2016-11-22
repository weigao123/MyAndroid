package com.garfield.weishu.discovery.news;


public class Urls {
    //http://blog.csdn.net/modalyin/article/details/51509620

    //新闻列表
    //http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html

    //Head指向的网址
    //http://c.m.163.com/photo/api/set/0001/2212951.json

    public static final int PAGE_SIZE = 20;

    public static final String HOST = "http://c.m.163.com/";
    public static final String HOST_NEWS_TOP = HOST + "nc/article/headline/";
    public static final String HOST_NEWS_COMMON = HOST + "nc/article/list/";
    public static final String HOST_NEWS_LOCAL = HOST + "nc/article/local/";
    public static final String HOST_NEWS_HOUSE = HOST + "nc/article/house/";

    public static final String TOP_ID = "T1348647909107";
    public static final String TECHNOLOGY_ID = "T1348649580692";
    public static final String ENTERTAINMENT_ID = "T1348648517839";
    public static final String FINANCE_ID = "T1348648756099";
    public static final String HEALTH_ID = "T1414389941036";
    public static final String EMOTION_ID = "T1348650839000";
    public static final String TIANJIN_ID = "5aSp5rSl";
    public static final String HOUSE_ID = "5aSp5rSl";

    public static final String END_LIST_URL = "-" + PAGE_SIZE + ".html";

    // 新闻详情
    public static final String NEW_DETAIL = HOST + "nc/article/";

    public static final String END_DETAIL_URL = "/full.html";







    // 图片
    public static final String IMAGES_URL = "http://api.laifudao.com/open/tupian.json";

    // 天气预报url
    public static final String WEATHER = "http://wthrcdn.etouch.cn/weather_mini?city=";

    //百度定位
    public static final String INTERFACE_LOCATION = "http://api.map.baidu.com/geocoder";

}
