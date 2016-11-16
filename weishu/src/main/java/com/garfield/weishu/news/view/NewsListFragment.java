package com.garfield.weishu.news.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.base.recyclerview.RecyclerUtil;
import com.garfield.weishu.base.recyclerview.TRecyclerAdapter;
import com.garfield.weishu.base.viewpager.TPagerAdapter;
import com.garfield.weishu.news.bean.NewsBean;
import com.garfield.weishu.news.head.NewsHeadView;
import com.garfield.weishu.news.presenter.NewsPresenter;
import com.garfield.weishu.news.presenter.NewsPresenterImpl;
import com.garfield.weishu.news.presenter.NewsView;
import com.garfield.weishu.ui.fragment.AppBaseFragment;
import com.garfield.weishu.utils.cache.ACache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by gaowei3 on 2016/10/28.
 */

public class NewsListFragment extends AppBaseFragment implements
        PullToRefreshView.OnPullRefreshListener, NewsView<NewsBean>,
        View.OnClickListener, TRecyclerAdapter.ItemEventListener<NewsBean>, TPagerAdapter.ItemEventListener<NewsBean> {

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
    private View footRefreshing;
    private View footRefreshFailed;
    private NewsListRecyclerAdapter mNewsListRecyclerAdapter;
    private List<NewsBean> mItems = new ArrayList<>();

    private NewsPresenter mNewsPresenter;

    /**
     * 当前已有的页数
     */
    private int pageIndex = 0;
    /**
     * 如果是true，要把PullView显示
     */
    private boolean isLoadAll;

    private String aTag = "data_list";
    private ACache aCache;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        aCache = ACache.get(mActivity);
        mPullToRefreshView.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mReloadBtn.setOnClickListener(this);

        mNewsHeadView = new NewsHeadView(getContext());
        mNewsHeadView.setItemEventListener(this);
        View footView = LayoutInflater.from(getContext()).inflate(R.layout.view_footer, (ViewGroup) rootView, false);
        footRefreshing = footView.findViewById(R.id.foot_refreshing);
        footRefreshFailed = footView.findViewById(R.id.foot_refresh_failed);
        footRefreshFailed.setOnClickListener(this);
        mNewsListRecyclerAdapter = new NewsListRecyclerAdapter(getContext(), mItems);
        mNewsListRecyclerAdapter.setHeadView(mNewsHeadView);
        mNewsListRecyclerAdapter.setFootView(footView);
        mNewsListRecyclerAdapter.setItemEventListener(this);
        mRecyclerView.setAdapter(mNewsListRecyclerAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);

        mNewsPresenter = new NewsPresenterImpl(this);
        refreshNewsListFromACache();
        if (pageIndex == 0) {
            refreshAll();
        }
    }

    @Override
    public void onLoadBefore() {
        if (isLoadAll) {
            mPullToRefreshView.setRefreshState(true);
        }
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                    RecyclerUtil.isAtBottom(recyclerView) &&
                    !mNewsListRecyclerAdapter.isFootVisible()) {
                refreshMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        }
    };

    @Override
    public void onLoadSuccess(List<NewsBean> data) {
        refreshData(data);
        putNewsListToACache();
    }

    private void refreshData(List<NewsBean> data) {
        List<NewsBean> slideItem = new ArrayList<>();
        List<NewsBean> normalItem = new ArrayList<>();
        separateItems(data, slideItem, normalItem);
        if (isLoadAll) {
            mNewsHeadView.refreshItems(slideItem);
            mNewsListRecyclerAdapter.refreshItems(normalItem);
            pageIndex = 1;
        } else {
            mNewsListRecyclerAdapter.addItems(normalItem);
            ++ pageIndex;
        }
        endRefresh(true);
    }

    @Override
    public void onLoadFailed() {
        endRefresh(false);
    }

    @Override
    public void onPullRefresh() {
        refreshAll();
    }

    private void separateItems(List<NewsBean> data, List<NewsBean> slideItem, List<NewsBean> normalItem) {
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
    }

    private void refreshAll() {
        isLoadAll = true;
        mNewsPresenter.loadNews(0, 0);
    }

    private void refreshMore() {
        mNewsListRecyclerAdapter.setFootVisible(true);
        footRefreshing.setVisibility(View.VISIBLE);
        footRefreshFailed.setVisibility(View.GONE);
        mNewsPresenter.loadNews(0, pageIndex);
    }

    private void endRefresh(boolean isSuccess) {
        isLoadAll = false;
        if (pageIndex == 0) {
            mPullToRefreshView.setVisibility(View.GONE);
            mReloadBtn.setVisibility(View.VISIBLE);
            mNewsListRecyclerAdapter.setFootVisible(false);
        } else {
            mPullToRefreshView.setVisibility(View.VISIBLE);
            mReloadBtn.setVisibility(View.GONE);
            if (isSuccess) {
                mNewsListRecyclerAdapter.setFootVisible(false);
            } else {
                mNewsListRecyclerAdapter.setFootVisible(true);
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

    private void putNewsListToACache() {
        List<NewsBean> allData = new ArrayList<>();
        allData.addAll(mNewsHeadView.getItems());
        allData.addAll(mNewsListRecyclerAdapter.getItems());
        JSONArray itemJsonAr = new JSONArray();
        for (NewsBean item : allData) {
            JSONObject jsonObject = item.toJSONObj();
            itemJsonAr.put(jsonObject);
        }
        JSONObject itemJsonObj = new JSONObject();
        try {
            itemJsonObj.put("news_list", itemJsonAr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        aCache.put(aTag, itemJsonObj);
    }

    private void refreshNewsListFromACache() {
        JSONObject jsonObject = aCache.getAsJSONObject(aTag);
        if (jsonObject == null) {
            return;
        }
        List<NewsBean> newsList = new ArrayList<>();
        try {
            JSONArray itemAr = jsonObject.getJSONArray("news_list");
            for (int i = 0; i < itemAr.length(); i++) {
                JSONObject itemObj = itemAr.getJSONObject(i);
                NewsBean item = NewsBean.parse(itemObj);
                newsList.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        isLoadAll = true;
        refreshData(newsList);
    }


    @Override
    public void onItemClick(NewsBean item) {
        EventDispatcher.getFragmentJumpEvent().onShowNewsDetail(item.getDocid());
    }

    @Override
    public void onItemLongPressed(NewsBean item) {

    }
}