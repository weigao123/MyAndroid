package com.garfield.weishu.discovery.news.model;

import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.base.OnMyRequestListener;
import com.garfield.weishu.discovery.news.view.NewsFragment;
import com.garfield.weishu.helper.http.okhttp.OkHttp3Utils;
import com.garfield.weishu.discovery.news.Urls;
import com.garfield.weishu.discovery.news.bean.NewsBean;
import com.garfield.weishu.discovery.news.bean.NewsDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaowei3 on 2016/11/10.
 */

public class NewsModelImpl implements NewsModel {


    @Override
    public void loadNews(String url, final int type, final OnMyRequestListener<NewsBean> listener) {
        L.d("loadNews url: " + url);
        OkHttp3Utils.OkHttpResultCallback<String> loadNewsCallback = new OkHttp3Utils.OkHttpResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                L.d("loadNews response: "+response);
                List<NewsBean> newsBeanList = NewsJsonUtils.readJsonNewsBeans(response, getID(type));
                listener.onSuccess(newsBeanList);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure(e);
            }
        };
        OkHttp3Utils.get(url, loadNewsCallback);
//        List<OkHttp3Utils.Param> params = new ArrayList<>();
//        OkHttp3Utils.Param param = new OkHttp3Utils.Param("Content-Type", "application/json;charset=utf-8");
//        params.add(param);
//        OkHttp3Utils.post(url, loadNewsCallback, params);
    }

    @Override
    public void loadNewsDetail(final String docid, final OnMyRequestListener<NewsDetailBean> listener) {
        String url = getDetailUrl(docid);
        L.d("loadNewsDetail url: " + url);
        OkHttp3Utils.OkHttpResultCallback<String> loadNewsCallback = new OkHttp3Utils.OkHttpResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                L.d("loadNewsDetail response: " + response);
                List<NewsDetailBean> newsDetailBeanList = new ArrayList<>();
                NewsDetailBean newsDetailBean = NewsJsonUtils.readJsonNewsDetailBeans(response, docid);
                newsDetailBeanList.add(newsDetailBean);
                listener.onSuccess(newsDetailBeanList);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure(e);
            }
        };
        OkHttp3Utils.get(url, loadNewsCallback);
    }

    private String getID(int type) {
        String id;
        switch (type) {
            case NewsFragment.NEWS_TYPE_TOP:
                id = Urls.TOP_ID;
                break;
            case NewsFragment.NEWS_TYPE_TIANJIN:
                id = "天津";
                break;
            case NewsFragment.NEWS_TYPE_TECHNOLOGY:
                id = Urls.TECHNOLOGY_ID;
                break;
            case NewsFragment.NEWS_TYPE_ENTERTAINMENT:
                id = Urls.ENTERTAINMENT_ID;
                break;
            case NewsFragment.NEWS_TYPE_FINANCE:
                id = Urls.FINANCE_ID;
                break;
            case NewsFragment.NEWS_TYPE_HOUSE:
                id = "天津";
                break;
            case NewsFragment.NEWS_TYPE_HEALTH:
                id = Urls.HEALTH_ID;
                break;
            case NewsFragment.NEWS_TYPE_EMOTION:
                id = Urls.EMOTION_ID;
                break;
            default:
                id = Urls.TOP_ID;
                break;
        }
        return id;
    }

    private String getDetailUrl(String docId) {
        StringBuffer sb = new StringBuffer(Urls.NEW_DETAIL);
        sb.append(docId).append(Urls.END_DETAIL_URL);
        return sb.toString();
    }
}
