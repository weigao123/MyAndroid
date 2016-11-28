package com.garfield.weishu.discovery.news.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.weishu.discovery.browser.BrowserFragment;
import com.garfield.weishu.discovery.news.bean.NewsDetailBean;
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

    {
        setAnimationEnable(false);
    }

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

        String docid = getArguments().getString(NEWS_DOC_ID);
        NewsPresenter newsPresenter = new NewsPresenterImpl(this);
        newsPresenter.loadNewsDetail(docid);
    }

    @Override
    public void onLoadBefore() {
    }

    @Override
    public void onLoadSuccess(List<NewsDetailBean> data) {
        NewsDetailBean bean = data.get(0);
        if (bean != null) {
            String titleString = "<font size=\"4.2\"><b>" + bean.getTitle() + "</b></font><br/>";
            String sourceString = "<font size=\"1\" color=\"gray\"><b>" + bean.getSource() + "&nbsp;&nbsp;&nbsp;&nbsp;" + bean.getPtime() + "</b></font><br/>";
            String content = titleString + sourceString + bean.getBody();

            loadRootFragment(R.id.fragment_detail_browser, BrowserFragment.newInstance(content, BrowserFragment.TYPE_DATA));

        }
    }

    @Override
    public void onLoadFailed() {
    }


}
