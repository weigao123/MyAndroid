package com.garfield.weishu.discovery.news.model;

import android.os.Handler;
import android.os.Looper;

import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.base.OnMyRequestListener;
import com.garfield.weishu.discovery.news.api.ApiManager;
import com.garfield.weishu.discovery.news.api.NeteaseApi;
import com.garfield.weishu.discovery.news.bean.netease.NewsBean;
import com.garfield.weishu.discovery.news.bean.netease.NewsDetailBean;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gaowei3 on 2016/11/10.
 */

public class NewsModelImpl implements NewsModel {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private CompositeSubscription mCompositeSubscription;


    @Override
    public void loadNews(final int type, int pageIndex, final OnMyRequestListener<NewsBean> listener) {
        String url = getUrl(type, pageIndex);
        L.d("loadNews url: " + url);

        Subscription subscription = ApiManager.getNeteaseManager().getNews(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<String>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(String response) {
                    L.d("loadNews response: "+response);
                    List<NewsBean> newsBeanList = NewsJsonUtils.readJsonNewsBeans(response, getID(type));
                    listener.onSuccess(newsBeanList);
                }
            });
        addSubscription(subscription);

//        OkHttp3Utils.OkHttpResultCallback<String> loadNewsCallback = new OkHttp3Utils.OkHttpResultCallback<String>() {
//            @Override
//            public void onSuccess(String response) {
//                L.d("loadNews response: "+response);
//                List<NewsBean> newsBeanList = NewsJsonUtils.readJsonNewsBeans(response, getID(type));
//                listener.onSuccess(newsBeanList);
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                listener.onFailure(e);
//            }
//        };
//        OkHttp3Utils.get(url, loadNewsCallback);

//        List<OkHttp3Utils.Param> params = new ArrayList<>();
//        OkHttp3Utils.Param param = new OkHttp3Utils.Param("Content-Type", "application/json;charset=utf-8");
//        params.add(param);
//        OkHttp3Utils.post(url, loadNewsCallback, params);
    }

    @Override
    public void loadNewsDetail(final String docid, final OnMyRequestListener<NewsDetailBean> listener) {
        String url = getDetailUrl(docid);
        L.d("loadNewsDetail url: " + url);

        Subscription subscription = ApiManager.getNeteaseManager().getNews(url)
            .subscribeOn(Schedulers.io())
            .subscribe(new Observer<String>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(String response) {
                    L.d("loadNewsDetail response: " + response);
                    List<NewsDetailBean> newsDetailBeanList = new ArrayList<>();
                    NewsDetailBean newsDetailBean = NewsJsonUtils.readJsonNewsDetailBeans(response, docid);
                    newsDetailBeanList.add(newsDetailBean);
                    listener.onSuccess(newsDetailBeanList);
                }
            });
        addSubscription(subscription);

//        OkHttp3Utils.OkHttpResultCallback<String> loadNewsCallback = new OkHttp3Utils.OkHttpResultCallback<String>() {
//            @Override
//            public void onSuccess(String response) {
//                L.d("loadNewsDetail response: " + response);
//                List<NewsDetailBean> newsDetailBeanList = new ArrayList<>();
//                NewsDetailBean newsDetailBean = NewsJsonUtils.readJsonNewsDetailBeans(response, docid);
//                newsDetailBeanList.add(newsDetailBean);
//                listener.onSuccess(newsDetailBeanList);
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                listener.onFailure(e);
//            }
//        };
//        OkHttp3Utils.get(url, loadNewsCallback);
    }

    @Override
    public void cancel() {
        unsubcrible();
    }

    private String getUrl(int type, int pageIndex) {
        StringBuilder sb = new StringBuilder();
        switch (type) {
            case NeteaseApi.NEWS_TYPE_TOP:
                sb.append(NeteaseApi.HOST_NEWS_TOP).append(NeteaseApi.TOP_ID);
                break;
            case NeteaseApi.NEWS_TYPE_TIANJIN:
                sb.append(NeteaseApi.HOST_NEWS_LOCAL).append(NeteaseApi.TIANJIN_ID);
                break;
            case NeteaseApi.NEWS_TYPE_TECHNOLOGY:
                sb.append(NeteaseApi.HOST_NEWS_COMMON).append(NeteaseApi.TECHNOLOGY_ID);
                break;
            case NeteaseApi.NEWS_TYPE_ENTERTAINMENT:
                sb.append(NeteaseApi.HOST_NEWS_COMMON).append(NeteaseApi.ENTERTAINMENT_ID);
                break;
            case NeteaseApi.NEWS_TYPE_FINANCE:
                sb.append(NeteaseApi.HOST_NEWS_COMMON).append(NeteaseApi.FINANCE_ID);
                break;
            case NeteaseApi.NEWS_TYPE_HOUSE:
                sb.append(NeteaseApi.HOST_NEWS_HOUSE).append(NeteaseApi.HOUSE_ID);
                break;
            case NeteaseApi.NEWS_TYPE_HEALTH:
                sb.append(NeteaseApi.HOST_NEWS_COMMON).append(NeteaseApi.HEALTH_ID);
                break;
            case NeteaseApi.NEWS_TYPE_EMOTION:
                sb.append(NeteaseApi.HOST_NEWS_COMMON).append(NeteaseApi.EMOTION_ID);
                break;
            default:
                sb.append(NeteaseApi.HOST_NEWS_COMMON).append(NeteaseApi.TOP_ID);
                break;
        }
        sb.append("/").append(pageIndex * NeteaseApi.PAGE_SIZE).append(NeteaseApi.END_LIST_URL);
        return sb.toString();
    }

    private String getID(int type) {
        String id;
        switch (type) {
            case NeteaseApi.NEWS_TYPE_TOP:
                id = NeteaseApi.TOP_ID;
                break;
            case NeteaseApi.NEWS_TYPE_TIANJIN:
                id = "天津";
                break;
            case NeteaseApi.NEWS_TYPE_TECHNOLOGY:
                id = NeteaseApi.TECHNOLOGY_ID;
                break;
            case NeteaseApi.NEWS_TYPE_ENTERTAINMENT:
                id = NeteaseApi.ENTERTAINMENT_ID;
                break;
            case NeteaseApi.NEWS_TYPE_FINANCE:
                id = NeteaseApi.FINANCE_ID;
                break;
            case NeteaseApi.NEWS_TYPE_HOUSE:
                id = "天津";
                break;
            case NeteaseApi.NEWS_TYPE_HEALTH:
                id = NeteaseApi.HEALTH_ID;
                break;
            case NeteaseApi.NEWS_TYPE_EMOTION:
                id = NeteaseApi.EMOTION_ID;
                break;
            default:
                id = NeteaseApi.TOP_ID;
                break;
        }
        return id;
    }

    private String getDetailUrl(String docId) {
        return NeteaseApi.NEW_DETAIL + docId + NeteaseApi.END_DETAIL_URL;
    }


    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    public void unsubcrible() {
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }
}
