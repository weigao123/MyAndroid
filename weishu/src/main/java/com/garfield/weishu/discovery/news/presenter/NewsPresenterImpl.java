package com.garfield.weishu.discovery.news.presenter;

import android.widget.Toast;

import com.garfield.baselib.utils.system.NetworkUtil;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.base.OnMyRequestListener;
import com.garfield.weishu.discovery.news.Urls;
import com.garfield.weishu.discovery.news.bean.NewsBean;
import com.garfield.weishu.discovery.news.bean.NewsDetailBean;
import com.garfield.weishu.discovery.news.model.NewsModel;
import com.garfield.weishu.discovery.news.model.NewsModelImpl;
import com.garfield.weishu.discovery.news.view.NewsFragment;

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
        String url = getUrl(type, pageIndex);
        if (!NetworkUtil.isNetAvailable(AppCache.getContext())) {
            Toast.makeText(AppCache.getContext(), R.string.status_network_is_not_available, Toast.LENGTH_SHORT).show();
            mNewsView.onLoadFailed();
            return;
        }
        mNewsView.onLoadBefore();
        mNewsModel.loadNews(url, type, new OnMyRequestListener<NewsBean>() {
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

    @Override
    public void loadNewsDetail(String docId) {
        if (!NetworkUtil.isNetAvailable(AppCache.getContext())) {
            Toast.makeText(AppCache.getContext(), R.string.status_network_is_not_available, Toast.LENGTH_SHORT).show();
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


    private String getUrl(int type, int pageIndex) {
        StringBuffer sb = new StringBuffer();
        switch (type) {
            case NewsFragment.NEWS_TYPE_TOP:
                sb.append(Urls.HOST_NEWS_TOP).append(Urls.TOP_ID);
                break;
            case NewsFragment.NEWS_TYPE_TIANJIN:
                sb.append(Urls.HOST_NEWS_LOCAL).append(Urls.TIANJIN_ID);
                break;
            case NewsFragment.NEWS_TYPE_TECHNOLOGY:
                sb.append(Urls.HOST_NEWS_COMMON).append(Urls.TECHNOLOGY_ID);
                break;
            case NewsFragment.NEWS_TYPE_ENTERTAINMENT:
                sb.append(Urls.HOST_NEWS_COMMON).append(Urls.ENTERTAINMENT_ID);
                break;
            case NewsFragment.NEWS_TYPE_FINANCE:
                sb.append(Urls.HOST_NEWS_COMMON).append(Urls.FINANCE_ID);
                break;
            case NewsFragment.NEWS_TYPE_HOUSE:
                sb.append(Urls.HOST_NEWS_HOUSE).append(Urls.HOUSE_ID);
                break;
            case NewsFragment.NEWS_TYPE_HEALTH:
                sb.append(Urls.HOST_NEWS_COMMON).append(Urls.HEALTH_ID);
                break;
            case NewsFragment.NEWS_TYPE_EMOTION:
                sb.append(Urls.HOST_NEWS_COMMON).append(Urls.EMOTION_ID);
                break;
            default:
                sb.append(Urls.HOST_NEWS_COMMON).append(Urls.TOP_ID);
                break;
        }
        sb.append("/").append(pageIndex * Urls.PAGE_SIZE).append(Urls.END_LIST_URL);
        return sb.toString();
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
