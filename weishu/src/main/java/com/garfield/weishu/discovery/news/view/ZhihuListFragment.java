package com.garfield.weishu.discovery.news.view;

import android.os.Bundle;
import android.view.View;

import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.base.recyclerview.TRecyclerAdapter;
import com.garfield.weishu.base.viewpager.TPagerAdapter;
import com.garfield.weishu.discovery.news.bean.zhihu.ZhihuDaily;

import java.util.List;

/**
 * Created by gaowei3 on 2016/12/5.
 */

public class ZhihuListFragment extends NewsListBaseFragment<ZhihuDaily> implements
        TRecyclerAdapter.ItemEventListener<ZhihuDaily>, TPagerAdapter.ItemEventListener<ZhihuDaily> {

    public static ZhihuListFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        ZhihuListFragment fragment = new ZhihuListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mIsVisibleToUser = true;
    }

    @Override
    protected void setupPrepare() {
        mRecyclerAdapter = new ZhihuListViewHolder.ZhihuRecyclerAdapter(AppCache.getContext(), mItems);
        mRecyclerAdapter.setItemEventListener(this);
    }

    @Override
    protected void loadDataFromCache() {

    }

    @Override
    protected void putDataToCache() {

    }

    @Override
    protected void refreshView(List<ZhihuDaily> data) {

    }

    @Override
    public void onItemClick(ZhihuDaily item, int position) {

    }

    @Override
    public void onItemLongPressed(ZhihuDaily item, int position) {

    }
}
