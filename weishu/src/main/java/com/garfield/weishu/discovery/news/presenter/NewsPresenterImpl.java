package com.garfield.weishu.discovery.news.presenter;

import com.garfield.baselib.utils.system.L;
import com.garfield.baselib.utils.system.NetworkUtil;
import com.garfield.weishu.R;
import com.garfield.weishu.base.OnMyRequestListener;
import com.garfield.weishu.discovery.news.api.ZhihuApi;
import com.garfield.weishu.discovery.news.bean.netease.NewsBean;
import com.garfield.weishu.discovery.news.bean.netease.NewsDetailBean;
import com.garfield.weishu.discovery.news.bean.zhihu.ZhihuDaily;
import com.garfield.weishu.discovery.news.model.NewsModel;
import com.garfield.weishu.discovery.news.model.NewsModelImpl;

import java.util.List;

/**
 * Created by gaowei3 on 2016/11/10.
 */

public class NewsPresenterImpl implements NewsPresenter {

    private NewsView mNewsView;
    private NewsModel mNewsModel;

    public NewsPresenterImpl(NewsView NewsView) {
        mNewsView = NewsView;
        mNewsModel = new NewsModelImpl();
    }

    /**
     * pageIndex，是起始item的索引，不是page
     */
    @Override
    public void loadNews(int type, int pageIndex) {
        if (!NetworkUtil.isNetAvailable()) {
            L.show(R.string.status_network_is_not_available);
            mNewsView.onLoadFailed();
            return;
        }
        mNewsView.onLoadBefore();
        if (type == ZhihuApi.NEWS_TYPE_ZHIHU) {
            mNewsModel.loadZhihu(pageIndex, new OnMyRequestListener<ZhihuDaily>() {
                @Override
                public void onSuccess(List<ZhihuDaily> data) {
                    mNewsView.onLoadSuccess(data);
                }

                @Override
                public void onFailure(Exception e) {
                    mNewsView.onLoadFailed();
                }
            });
        } else {
            mNewsModel.loadNews(type, pageIndex, new OnMyRequestListener<NewsBean>() {
                @Override
                public void onSuccess(List<NewsBean> data) {
                    mNewsView.onLoadSuccess(data);
                }

                @Override
                public void onFailure(Exception e) {
                    mNewsView.onLoadFailed();
                }
            });
        }
    }

    @Override
    public void loadNewsDetail(String docId) {
        if (!NetworkUtil.isNetAvailable()) {
            L.show(R.string.status_network_is_not_available);
            mNewsView.onLoadFailed();
            return;
        }
        mNewsView.onLoadBefore();
        mNewsModel.loadNewsDetail(docId, new OnMyRequestListener<NewsDetailBean>() {
            @Override
            public void onSuccess(List<NewsDetailBean> data) {
                handleString(data.get(0));
                mNewsView.onLoadSuccess(data);
            }

            @Override
            public void onFailure(Exception e) {
                mNewsView.onLoadFailed();
            }
        });
    }

    @Override
    public void cancel() {
        mNewsModel.cancel();
    }

    private void handleString(NewsDetailBean bean) {
        if (bean == null) return;
        String body = bean.getBody();
        int i = 0;
        while (true) {
            String imgTag = "<!--IMG#" + i +"-->";
            if (body.contains(imgTag)) {
                String imgUrl = bean.getImg().get(i).getSrc();
                String realImgTag = "<img src=\"" + imgUrl + "\" width=\"100%\">";
                body = body.replace(imgTag, realImgTag);
                ++i;
            } else {
                break;
            }
        }
        bean.setBody(body);
    }


}
