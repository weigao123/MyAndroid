package com.garfield.weishu.discovery.news.model;

import com.garfield.weishu.discovery.news.bean.netease.NewsBean;
import com.garfield.weishu.discovery.news.bean.netease.NewsDetailBean;
import com.garfield.baselib.utils.array.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class NewsJsonUtils {

    private final static String TAG = "NewsJsonUtils";

    public static List<NewsBean> readJsonNewsBeans(String res, String value) {
        List<NewsBean> beans = new ArrayList<NewsBean>();
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(res).getAsJsonObject();
            JsonElement jsonElement = jsonObj.get(value);
            if(jsonElement == null) {
                return null;
            }
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                if (jo.has("ads")) {
                    JsonElement element = jo.get("ads");
                    JsonArray array = element.getAsJsonArray();
                    for (JsonElement headElement : array) {
                        JsonObject headObject = headElement.getAsJsonObject();
                        NewsBean news = JsonUtils.deserialize(headObject, NewsBean.class);
                        news.setBeanType(NewsBean.TYPE_SLIDE_HEAD);
                        beans.add(news);
                    }
                    continue;
                }
                if (jo.has("skipType") && "special".equals(jo.get("skipType").getAsString())) {
                    continue;
                }
                if (jo.has("TAGS") && !jo.has("TAG")) {
                    continue;
                }

                if (!jo.has("imgextra")) {
                    NewsBean news = JsonUtils.deserialize(jo, NewsBean.class);
                    news.setBeanType(NewsBean.TYPE_NORMAL);
                    beans.add(news);
                }
            }
        } catch (Exception e) {
        }
        return beans;
    }

    public static NewsDetailBean readJsonNewsDetailBeans(String res, String docId) {
        NewsDetailBean newsDetailBean = null;
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(res).getAsJsonObject();
            JsonElement jsonElement = jsonObj.get(docId);
            if(jsonElement == null) {
                return null;
            }
            newsDetailBean = JsonUtils.deserialize(jsonElement.getAsJsonObject(), NewsDetailBean.class);
        } catch (Exception e) {
        }
        return newsDetailBean;
    }

}
