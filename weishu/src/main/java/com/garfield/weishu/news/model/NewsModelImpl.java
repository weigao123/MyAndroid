package com.garfield.weishu.news.model;

import com.garfield.baselib.utils.L;
import com.garfield.weishu.base.OnMyRequestListener;
import com.garfield.weishu.http.okhttp.OkHttp3Utils;
import com.garfield.weishu.news.Urls;
import com.garfield.weishu.news.bean.NewsBean;
import com.garfield.weishu.news.bean.NewsDetailBean;
import com.garfield.weishu.news.view.NewsListFragment;

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
                List<NewsBean> newsBeanList = NewsJsonUtils.readJsonNewsBeans(response, getID(type));
                listener.onSuccess(newsBeanList);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure(e);
            }
        };
        OkHttp3Utils.get(url, loadNewsCallback);
    }

    @Override
    public void loadNewsDetail(final String docid, final OnMyRequestListener<NewsDetailBean> listener) {
        String url = getDetailUrl(docid);
        L.d("loadNewsDetail url: " + url);
        OkHttp3Utils.OkHttpResultCallback<String> loadNewsCallback = new OkHttp3Utils.OkHttpResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
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
            case NewsListFragment.NEWS_TYPE_TOP:
                id = Urls.TOP_ID;
                break;
            case NewsListFragment.NEWS_TYPE_NBA:
                id = Urls.NBA_ID;
                break;
            case NewsListFragment.NEWS_TYPE_CARS:
                id = Urls.CAR_ID;
                break;
            case NewsListFragment.NEWS_TYPE_JOKES:
                id = Urls.JOKE_ID;
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
