package com.garfield.weishu.discovery.news.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.garfield.baselib.adapter.DividerItemDecoration;
import com.garfield.baselib.utils.cache.ACache;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.base.recyclerview.TRecyclerAdapter;
import com.garfield.weishu.base.viewpager.TPagerAdapter;
import com.garfield.weishu.discovery.browser.BrowserFragment;
import com.garfield.weishu.discovery.news.bean.netease.NewsBean;
import com.garfield.weishu.discovery.news.head.NewsHeadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gaowei3 on 2016/10/28.
 */

public class NewsListFragment extends NewsListBaseFragment<NewsBean> implements
        TRecyclerAdapter.ItemEventListener<NewsBean>, TPagerAdapter.ItemEventListener<NewsBean> {

    private NewsHeadView mNewsHeadView;
    private String mACacheTag;
    private ACache mACache;

    private List<NewsBean> mItems = new ArrayList<>();

    public static NewsListFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setupPrepare() {
        mACache = ACache.get(AppCache.getContext());
        mACacheTag = "data_list_" + mType;

        mRecyclerAdapter = new NewsListViewHolder.NewsListRecyclerAdapter(AppCache.getContext(), mItems);
        mNewsHeadView = new NewsHeadView(AppCache.getContext());
        mNewsHeadView.setItemEventListener(this);
        mRecyclerAdapter.setHeadView(mNewsHeadView);
        mRecyclerAdapter.setItemEventListener(this);
    }

    @Override
    protected void refreshView(List<NewsBean> data) {
        List<NewsBean> slideItem = new ArrayList<>();
        List<NewsBean> normalItem = new ArrayList<>();
        separateItems(data, slideItem, normalItem);
        if (isLoadAll) {
            mNewsHeadView.refreshItems(slideItem);
            mRecyclerAdapter.refreshItems(normalItem);
        } else {
            mRecyclerAdapter.addItems(normalItem);
        }
    }

    @Override
    protected void loadAll() {
        mNewsPresenter.loadNews(mType, 0);
    }

    @Override
    protected void loadMore() {
        mNewsPresenter.loadNews(mType, pageIndex);
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

    @Override
    public void loadDataFromCache() {
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
    public void putDataToCache() {
        List<NewsBean> allData = new ArrayList<>();
        allData.addAll(mNewsHeadView.getItems());
        allData.addAll(mRecyclerAdapter.getItems());
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

}
