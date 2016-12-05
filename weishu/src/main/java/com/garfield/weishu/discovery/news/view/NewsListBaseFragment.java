package com.garfield.weishu.discovery.news.view;

import android.view.View;

import com.garfield.weishu.base.recyclerview.TRecyclerAdapter;
import com.garfield.weishu.base.viewpager.TPagerAdapter;
import com.garfield.weishu.discovery.news.bean.netease.NewsBean;
import com.garfield.weishu.discovery.news.presenter.NewsView;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.List;

/**
 * Created by gaowei3 on 2016/12/5.
 */

public class NewsListBaseFragment<T> extends AppBaseFragment implements
        PullToRefreshView.OnPullRefreshListener, NewsView<T>,
        View.OnClickListener, TRecyclerAdapter.ItemEventListener<T>, TPagerAdapter.ItemEventListener<T> {
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(T item, int position) {

    }

    @Override
    public void onItemLongPressed(T item, int position) {

    }

    @Override
    public void onLoadBefore() {

    }

    @Override
    public void onLoadSuccess(List<T> data) {

    }

    @Override
    public void onLoadFailed() {

    }

    @Override
    public void onPullRefresh() {

    }
}
