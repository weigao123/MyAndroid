package com.garfield.weishu.discovery.news.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.garfield.baselib.utils.html.WebUtil;
import com.garfield.weishu.R;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.discovery.browser.BrowserFragment;
import com.garfield.weishu.discovery.news.bean.zhihu.ZhihuStory;
import com.garfield.weishu.discovery.news.presenter.NewsPresenter;
import com.garfield.weishu.discovery.news.presenter.NewsPresenterImpl;
import com.garfield.weishu.discovery.news.presenter.NewsView;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/11/13.
 */

public class ZhihuDetailFragment extends AppBaseFragment implements NewsView<ZhihuStory> {

    public static final String NEWS_DOC_ID = "news_doc_id";
    public static final String NEWS_IS_3W = "news_is_3w";

    @BindView(R.id.fragment_detail_browser)
    FrameLayout mBrowserContainer;

    private boolean mIs3w;

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
        return getString(R.string.zhihu_daily);
    }

    public static ZhihuDetailFragment newInstance(String url, boolean is3w) {
        Bundle args = new Bundle();
        args.putString(NEWS_DOC_ID, url);
        args.putBoolean(NEWS_IS_3W, is3w);
        ZhihuDetailFragment fragment = new ZhihuDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);

        String docid = getArguments().getString(NEWS_DOC_ID);
        mIs3w = getArguments().getBoolean(NEWS_IS_3W);
        mNewsPresenter = new NewsPresenterImpl(this);
        mNewsPresenter.loadZhihuDetail(docid);
    }

    @Override
    public void onLoadBefore() {
    }

    @Override
    public void onLoadSuccess(List<ZhihuStory> data) {
        ZhihuStory bean = data.get(0);
        if (bean != null) {
            if (isAdded()) {
                if (!TextUtils.isEmpty(bean.getBody()) && !mIs3w) {
                    String content = WebUtil.buildHtmlWithCss(bean.getBody(), bean.getCss(), AppCache.isNightMode());
                    loadRootFragment(R.id.fragment_detail_browser, BrowserFragment.newInstance(content, BrowserFragment.TYPE_STRING));
                } else {
                    loadRootFragment(R.id.fragment_detail_browser, BrowserFragment.newInstance(bean.getShareUrl(), BrowserFragment.TYPE_URL));
                }
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
}
