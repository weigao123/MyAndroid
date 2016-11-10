package com.garfield.weishu.news.presenter;

import com.garfield.weishu.base.OnMyRequestListener;
import com.garfield.weishu.news.Urls;
import com.garfield.weishu.news.bean.NewsBean;
import com.garfield.weishu.news.bean.NewsDetailBean;
import com.garfield.weishu.news.model.NewsModel;
import com.garfield.weishu.news.model.NewsModelImpl;
import com.garfield.weishu.news.view.NewsListFragment;

import java.util.List;

/**
 * Created by gaowei3 on 2016/11/10.
 */

public class NewsPresenterImpl implements NewsPresenter {

    private NewsView mNewsView;
    private NewsModel mNewsModel;

    public NewsPresenterImpl(NewsView newsView) {
        mNewsView = newsView;
        mNewsModel = new NewsModelImpl();
    }

    @Override
    public void loadNews(int type, int pageIndex) {
        String url = getUrl(type, pageIndex);
        mNewsModel.loadNews(url, type, new OnMyRequestListener<NewsBean>() {

            @Override
            public void onSuccess(List<NewsBean> data) {
                mNewsView.dataLoaded(data);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    @Override
    public void loadNewsDetail(String docId) {
        mNewsModel.loadNewsDetail(docId, new OnMyRequestListener<NewsDetailBean>() {

            @Override
            public void onSuccess(List<NewsDetailBean> data) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }


    private String getUrl(int type, int pageIndex) {
        StringBuffer sb = new StringBuffer();
        switch (type) {
            case NewsListFragment.NEWS_TYPE_TOP:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
            case NewsListFragment.NEWS_TYPE_NBA:
                sb.append(Urls.COMMON_URL).append(Urls.NBA_ID);
                break;
            case NewsListFragment.NEWS_TYPE_CARS:
                sb.append(Urls.COMMON_URL).append(Urls.CAR_ID);
                break;
            case NewsListFragment.NEWS_TYPE_JOKES:
                sb.append(Urls.COMMON_URL).append(Urls.JOKE_ID);
                break;
            default:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
        }
        sb.append("/").append(pageIndex).append(Urls.END_URL);
        return sb.toString();
    }
}
