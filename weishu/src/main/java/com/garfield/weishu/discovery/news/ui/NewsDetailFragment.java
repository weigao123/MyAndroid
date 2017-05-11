package com.garfield.weishu.discovery.news.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.garfield.weishu.R;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.discovery.browser.BrowserFragment;
import com.garfield.weishu.discovery.news.bean.netease.NewsDetailBean;
import com.garfield.weishu.discovery.news.presenter.NewsPresenter;
import com.garfield.weishu.discovery.news.presenter.NewsPresenterImpl;
import com.garfield.weishu.discovery.news.presenter.NewsView;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/11/13.
 */

public class NewsDetailFragment extends AppBaseFragment implements NewsView<NewsDetailBean> {

    public static final String NEWS_DOC_ID = "news_doc_id";

    @BindView(R.id.fragment_detail_browser)
    FrameLayout mBrowserContainer;

    private NewsPresenter mNewsPresenter;

    {
        setAnimationEnable(false);
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_news_detail;
    }

    @Override
    protected String onGetToolbarTitleResource() {
        return getString(R.string.netease_news);
    }

    public static NewsDetailFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(NEWS_DOC_ID, url);
        NewsDetailFragment fragment = new NewsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);

        String docid = getArguments().getString(NEWS_DOC_ID);
        mNewsPresenter = new NewsPresenterImpl(this);
        mNewsPresenter.loadNewsDetail(docid);
    }

    @Override
    public void onLoadBefore() {
    }

    @Override
    public void onLoadSuccess(List<NewsDetailBean> data) {
        handleString(data.get(0));
        NewsDetailBean bean = data.get(0);
        if (bean != null) {
            String titleString = "<tit><font size=\"4.2\"><b>" + bean.getTitle() + "</b></font><br/></tit>";
            String sourceString = "<sour><font size=\"1\"><b>" + bean.getSource() + "&nbsp;&nbsp;&nbsp;&nbsp;" + bean.getPtime() + "</b></font><br/></sour>";
            String content = titleString + sourceString + bean.getBody();
            if (isAdded()) {
                loadRootFragment(R.id.fragment_detail_browser, BrowserFragment.newInstance(content, BrowserFragment.TYPE_STRING, 120));
            }
        }
    }

    @Override
    public void onLoadFailed() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mNewsPresenter != null) {
            mNewsPresenter.cancel();
        }
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
