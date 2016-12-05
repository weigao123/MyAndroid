package com.garfield.weishu.discovery.news.view;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.garfield.baselib.adapter.DividerItemDecoration;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.base.recyclerview.RecyclerUtil;
import com.garfield.weishu.base.recyclerview.TRecyclerAdapter;
import com.garfield.weishu.base.viewpager.TPagerAdapter;
import com.garfield.weishu.discovery.browser.BrowserFragment;
import com.garfield.weishu.discovery.news.api.ZhihuApi;
import com.garfield.weishu.discovery.news.bean.netease.NewsBean;
import com.garfield.weishu.discovery.news.head.NewsHeadView;
import com.garfield.weishu.discovery.news.presenter.NewsPresenter;
import com.garfield.weishu.discovery.news.presenter.NewsPresenterImpl;
import com.garfield.weishu.discovery.news.presenter.NewsView;
import com.garfield.weishu.ui.fragment.AppBaseFragment;
import com.garfield.baselib.utils.cache.ACache;

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

    private int mType;
    private NewsPresenter mNewsPresenter;

    /**
     * 当前已有的页数
     */
    private int pageIndex = 0;
    /**
     * 如果是true，要把PullView显示
     */
    private boolean isLoadAll;

    private String mACacheTag;
    private ACache mACache;

    public static NewsListFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        mType = getArguments().getInt("type");
        if (mType == ZhihuApi.NEWS_TYPE_ZHIHU) {
            mToolbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onLazyLoad() {
        mACache = ACache.get(AppCache.getContext());
        mACacheTag = "data_list_" + mType;
        mPullToRefreshView.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AppCache.getContext()));
        mReloadBtn.setOnClickListener(this);

        mNewsHeadView = new NewsHeadView(AppCache.getContext());
        mNewsHeadView.setItemEventListener(this);
        View footView = LayoutInflater.from(getContext()).inflate(R.layout.view_footer, (ViewGroup) mRootView, false);
        footRefreshing = footView.findViewById(R.id.foot_refreshing);
        footRefreshFailed = footView.findViewById(R.id.foot_refresh_failed);
        footRefreshFailed.setOnClickListener(this);
        mNewsListRecyclerAdapter = new NewsListRecyclerAdapter(AppCache.getContext(), mItems);
        if (mType == 0) {
            mNewsListRecyclerAdapter.setHeadView(mNewsHeadView);
        }
        mNewsListRecyclerAdapter.setFootView(footView);
        mNewsListRecyclerAdapter.setItemEventListener(this);
        mRecyclerView.setAdapter(mNewsListRecyclerAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);

        mNewsPresenter = new NewsPresenterImpl(this);
        refreshNewsListFromACache();
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshAll();
            }
        }, 1000);
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
        if (data.size() == 0) {
            Snackbar.make(mRootView, R.string.no_data, Snackbar.LENGTH_SHORT).show();
            endRefresh(true);
            return;
        }
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
        mNewsPresenter.loadNews(mType, 0);
    }

    private void refreshMore() {
        mNewsListRecyclerAdapter.setFootVisible(true);
        footRefreshing.setVisibility(View.VISIBLE);
        footRefreshFailed.setVisibility(View.GONE);
        mNewsPresenter.loadNews(mType, pageIndex);
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
        mACache.put(mACacheTag, itemJsonObj);
    }

    private void refreshNewsListFromACache() {
        JSONObject jsonObject = mACache.getAsJSONObject(mACacheTag);
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
    public void onItemClick(NewsBean item, int position) {
        EventDispatcher.getFragmentJumpEvent().onShowNewsDetail(item.getDocid());
    }

    @Override
    public void onItemLongPressed(final NewsBean item, int position) {
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .items(R.array.news_menu)
                .listSelector(R.drawable.bg_press_gray)
                .itemsColorRes(R.color.black)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        switch (position) {
                            case 0:
                                if (!TextUtils.isEmpty(item.getUrl_3w())) {
                                    EventDispatcher.startFragment(BrowserFragment.newInstance(item.getUrl_3w(), BrowserFragment.TYPE_URL));
                                } else {
                                    //L.show(R.string.no_data);
                                    Snackbar.make(mRootView, R.string.no_data, Snackbar.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                })
                .build();
        RecyclerView recyclerView = dialog.getRecyclerView();
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mNewsPresenter != null) {
            mNewsPresenter.cancel();
        }
    }
}
