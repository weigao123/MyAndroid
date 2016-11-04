package com.garfield.weishu.news;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.garfield.weishu.R;
import com.garfield.weishu.news.head.NewsHeadView;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/10/28.
 */

public class NewsFragment extends AppBaseFragment {

    @BindView(R.id.fragment_new_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.fragment_new_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.fragment_new_reload_btn)
    Button mReloadBtn;

    private NewsHeadView mNewsHeadView;
    private NewsRecyclerAdapter mNewsRecyclerAdapter;
    private List<String> mItems;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_new;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mItems = new ArrayList<>(5);
        mItems.add("");
        mItems.add("");
        mItems.add("");
        mNewsHeadView = new NewsHeadView(getContext());
        mNewsRecyclerAdapter = new NewsRecyclerAdapter(getContext(), mItems);
        mNewsRecyclerAdapter.setHeadView(mNewsHeadView);
        mRecyclerView.setAdapter(mNewsRecyclerAdapter);
    }
}
