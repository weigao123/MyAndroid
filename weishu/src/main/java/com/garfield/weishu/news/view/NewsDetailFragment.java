package com.garfield.weishu.news.view;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.garfield.weishu.R;
import com.garfield.weishu.news.bean.NewsDetailBean;
import com.garfield.weishu.news.presenter.NewsPresenter;
import com.garfield.weishu.news.presenter.NewsPresenterImpl;
import com.garfield.weishu.news.presenter.NewsView;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/11/13.
 */

public class NewsDetailFragment extends AppBaseFragment implements NewsView<NewsDetailBean> {

    public static final String NEWS_DOC_ID = "news_doc_id";

    @BindView(R.id.fragment_news_detail_title)
    TextView mTitle;

    @BindView(R.id.fragment_news_detail_source)
    TextView mSource;

    @BindView(R.id.fragment_news_detail_webview)
    WebView mWebView;

    private String mDocid;
    private NewsPresenter mNewsPresenter;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_news_detail;
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
        mDocid = getArguments().getString(NEWS_DOC_ID);

        mNewsPresenter = new NewsPresenterImpl(this);
        mNewsPresenter.loadNewsDetail(mDocid);
        WebSettings settings = mWebView.getSettings();
        settings.setSupportZoom(true);
        settings.setTextZoom(120);
    }

    @Override
    public void onLoadBefore() {

    }

    @Override
    public void onLoadSuccess(List<NewsDetailBean> data) {
        NewsDetailBean bean = data.get(0);
        mTitle.setText(bean.getTitle());
        mSource.setText(bean.getSource() + "  " + bean.getPtime());
        mWebView.loadData(bean.getBody(), "text/html; charset=UTF-8", null);
    }

    @Override
    public void onLoadFailed() {

    }
}
