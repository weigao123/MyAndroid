package com.garfield.weishu.news.presenter;

import android.widget.Toast;

import com.garfield.baselib.utils.NetworkUtil;
import com.garfield.weishu.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.base.OnMyRequestListener;
import com.garfield.weishu.news.Urls;
import com.garfield.weishu.news.bean.NewsBean;
import com.garfield.weishu.news.bean.NewsDetailBean;
import com.garfield.weishu.news.model.NewsModel;
import com.garfield.weishu.news.model.NewsModelImpl;
import com.garfield.weishu.news.view.NewsListFragment;
import com.nostra13.universalimageloader.utils.L;

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
        sb.append("/").append(pageIndex * Urls.PAZE_SIZE).append(Urls.END_URL);
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
