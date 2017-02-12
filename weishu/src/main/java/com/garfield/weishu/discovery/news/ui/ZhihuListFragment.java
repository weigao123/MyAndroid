package com.garfield.weishu.discovery.news.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.garfield.baselib.adapter.DividerItemDecoration;
import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.base.recyclerview.TRecyclerAdapter;
import com.garfield.weishu.base.viewpager.TPagerAdapter;
import com.garfield.weishu.discovery.news.bean.zhihu.ZhihuDaily;
import com.garfield.weishu.discovery.news.bean.zhihu.ZhihuDailyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaowei3 on 2016/12/5.
 */

public class ZhihuListFragment extends NewsListBaseFragment<ZhihuDaily> implements
        TRecyclerAdapter.ItemEventListener<ZhihuDailyItem>, TPagerAdapter.ItemEventListener<ZhihuDailyItem> {

    private List<ZhihuDailyItem> mItems = new ArrayList<>();
    private String mCurrentDate;

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
        mRecyclerAdapter = new ZhihuListViewHolder.ZhihuRecyclerAdapter(mActivity, mItems);
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
        mCurrentDate = data.get(0).getDate();
        if (isLoadAll) {
            mRecyclerAdapter.refreshItems(data.get(0).getStories());
        } else {
            mRecyclerAdapter.addItems(data.get(0).getStories());
        }
    }

    @Override
    protected void loadAll() {
        mNewsPresenter.loadZhihu(null);
    }

    @Override
    protected void loadMore() {
        mNewsPresenter.loadZhihu(mCurrentDate);
    }

    @Override
    public void onItemClick(ZhihuDailyItem item, int position) {
        EventDispatcher.startFragmentEvent(ZhihuDetailFragment.newInstance(item.getId(), false));
    }

    @Override
    public void onItemLongPressed(final ZhihuDailyItem item, int position) {
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .items(R.array.news_menu)
                .listSelector(R.drawable.bg_press_gray)
                .itemsColorRes(R.color.black)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        switch (position) {
                            case 0:
                                EventDispatcher.startFragmentEvent(ZhihuDetailFragment.newInstance(item.getId(), true));
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
