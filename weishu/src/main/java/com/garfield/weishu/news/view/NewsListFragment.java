package com.garfield.weishu.news.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.garfield.baselib.utils.L;
import com.garfield.weishu.R;
import com.garfield.weishu.news.Urls;
import com.garfield.weishu.news.bean.NewsBean;
import com.garfield.weishu.news.head.NewsHeadView;
import com.garfield.weishu.news.presenter.NewsPresenter;
import com.garfield.weishu.news.presenter.NewsPresenterImpl;
import com.garfield.weishu.news.presenter.NewsView;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by gaowei3 on 2016/10/28.
 */

public class NewsListFragment extends AppBaseFragment implements PullToRefreshView.OnPullRefreshListener, NewsView<NewsBean> {

    public static final int NEWS_TYPE_TOP = 0;
    public static final int NEWS_TYPE_NBA = 1;
    public static final int NEWS_TYPE_CARS = 2;
    public static final int NEWS_TYPE_JOKES = 3;

    @BindView(R.id.fragment_news_list_refresh)
    PullToRefreshView mPullToRefreshView;

    @BindView(R.id.fragment_news_list_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.fragment_news_list_reload_btn)
    Button mReloadBtn;

    private NewsHeadView mNewsHeadView;
    private View mFootView;
    private NewsRecyclerAdapter mNewsRecyclerAdapter;
    private List<NewsBean> mItems = new ArrayList<>();

    private NewsPresenter mNewsPresenter;
    private int pageIndex = 0;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mPullToRefreshView.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mNewsHeadView = new NewsHeadView(getContext());
        mFootView = LayoutInflater.from(getContext()).inflate(R.layout.view_footer, (ViewGroup) rootView, false);
        mNewsRecyclerAdapter = new NewsRecyclerAdapter(getContext(), mItems);
        mNewsRecyclerAdapter.setHeadView(mNewsHeadView);
        mNewsRecyclerAdapter.setFootView(mFootView);
        mRecyclerView.setAdapter(mNewsRecyclerAdapter);

        mNewsPresenter = new NewsPresenterImpl(this);
        mNewsPresenter.loadNews(0, pageIndex);
    }



    @Override
    public void startLoad() {

    }

    @Override
    public void dataLoaded(List<NewsBean> data) {
        List<NewsBean> slideItem = new ArrayList<>();
        List<NewsBean> normalItem = new ArrayList<>();
        for (NewsBean bean : data) {
            switch (bean.getBeanType()) {
                case NewsBean.TYPE_SLIDE_HEAD:
                    slideItem.add(bean);
                    break;
                case NewsBean.TYPE_NORMAL:
                    normalItem.add(bean);
                    break;
            }
        }
        if (!slideItem.isEmpty()) {
            mNewsHeadView.refreshItems(slideItem);
        }
        if (!normalItem.isEmpty()) {
            mNewsRecyclerAdapter.refreshItems(normalItem);
        }
        mPullToRefreshView.setRefreshing(false);
    }


    @Override
    public void loadFailed() {

    }


    @Override
    public void onRefresh() {
        pageIndex = 0;
        mNewsPresenter.loadNews(0, pageIndex);
    }
}
