package com.garfield.weishu.discovery.news.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.garfield.weishu.ui.view.PullToRefreshView;
import com.garfield.weishu.R;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.base.recyclerview.RecyclerUtil;
import com.garfield.weishu.base.recyclerview.BaseRecyclerAdapter;
import com.garfield.weishu.discovery.news.presenter.NewsPresenter;
import com.garfield.weishu.discovery.news.presenter.NewsPresenterImpl;
import com.garfield.weishu.discovery.news.presenter.NewsView;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/12/5.
 */

public abstract class NewsListBaseFragment<T> extends AppBaseFragment implements
        PullToRefreshView.OnPullRefreshListener, NewsView<T>,
        View.OnClickListener {

    @BindView(R.id.fragment_news_list_refresh)
    PullToRefreshView mPullToRefreshView;

    @BindView(R.id.fragment_news_list_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.fragment_news_list_reload_btn)
    Button mReloadBtn;

    private View footRefreshing;
    private View footRefreshFailed;
    protected BaseRecyclerAdapter mRecyclerAdapter;

    protected int mType;
    protected NewsPresenter mNewsPresenter;

    /**
     * 当前已有的页数
     */
    protected int pageIndex = 0;
    /**
     * 如果是true，要把PullView显示
     */
    protected boolean isLoadAll;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {

    }

    @Override
    protected void onLazyLoad() {
        mType = getArguments().getInt("type");

        setupPrepare();

        mPullToRefreshView.setOnRefreshListener(this);
        mReloadBtn.setOnClickListener(this);

        View footView = LayoutInflater.from(getContext()).inflate(R.layout.view_footer, (ViewGroup) mRootView, false);
        footRefreshing = footView.findViewById(R.id.foot_refreshing);
        footRefreshFailed = footView.findViewById(R.id.foot_refresh_failed);
        footRefreshFailed.setOnClickListener(this);
        mRecyclerAdapter.setFootView(footView);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AppCache.getContext()));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);

        mNewsPresenter = new NewsPresenterImpl(this);

        loadDataFromCache();
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshAll();
            }
        }, 500);
    }

    protected abstract void setupPrepare();
    protected abstract void loadDataFromCache();
    protected abstract void putDataToCache();
    protected abstract void refreshView(List<T> data);
    protected abstract void loadAll();
    protected abstract void loadMore();

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                    RecyclerUtil.isAtBottom(recyclerView) &&
                    !mRecyclerAdapter.isFootVisible()) {
                refreshMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        }
    };

    private void refreshAll() {
        isLoadAll = true;
        loadAll();
    }

    @Override
    public void onLoadBefore() {
        if (isLoadAll) {
            mPullToRefreshView.setRefreshState(true);
        }
    }

    @Override
    public void onLoadSuccess(List<T> data) {
        refreshData(data);
        putDataToCache();
    }

    @Override
    public void onLoadFailed() {
        endRefresh(false);
    }

    /**
     * 还有可能从Cache中加载后调用
     */
    public void refreshData(List<T> data) {
        if (data.size() == 0) {
            Snackbar.make(mRootView, R.string.no_data, Snackbar.LENGTH_SHORT).show();
            endRefresh(true);
            return;
        }
        refreshView(data);
        if (isLoadAll) {
            pageIndex = 1;
        } else {
            ++ pageIndex;
        }
        endRefresh(true);
    }

    @Override
    public void onPullRefresh() {
        refreshAll();
    }

    private void refreshMore() {
        mRecyclerAdapter.setFootVisible(true);
        footRefreshing.setVisibility(View.VISIBLE);
        footRefreshFailed.setVisibility(View.GONE);
        loadMore();
    }

    private void endRefresh(boolean isSuccess) {
        isLoadAll = false;
        if (pageIndex == 0) {
            mPullToRefreshView.setVisibility(View.GONE);
            mReloadBtn.setVisibility(View.VISIBLE);
            mRecyclerAdapter.setFootVisible(false);
        } else {
            mPullToRefreshView.setVisibility(View.VISIBLE);
            mReloadBtn.setVisibility(View.GONE);
            if (isSuccess) {
                mRecyclerAdapter.setFootVisible(false);
            } else {
                mRecyclerAdapter.setFootVisible(true);
                footRefreshing.setVisibility(View.GONE);
                footRefreshFailed.setVisibility(View.VISIBLE);
            }
        }
        mPullToRefreshView.setRefreshState(false);
    }

    @Override
    public void onClick(View v) {
        if (v == mReloadBtn) {
            refreshAll();
        } else if (v == footRefreshFailed) {
            footRefreshing.setVisibility(View.VISIBLE);
            footRefreshFailed.setVisibility(View.GONE);
            refreshMore();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mNewsPresenter != null) {
            //mNewsPresenter.cancel();
        }
    }
}
