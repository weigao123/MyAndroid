package com.garfield.weishu.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.garfield.baselib.utils.L;
import com.garfield.weishu.R;
import com.garfield.weishu.base.recyclerview.RecyclerUtil;
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
    PullToRefreshView_2 mPullToRefreshView;

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
        mItems.add("");
        mItems.add("");
        mItems.add("");
        mItems.add("");
        mNewsHeadView = new NewsHeadView(getContext());
        mNewsRecyclerAdapter = new NewsRecyclerAdapter(getContext(), mItems);
        mNewsRecyclerAdapter.setHeadView(mNewsHeadView);
        mRecyclerView.setAdapter(mNewsRecyclerAdapter);

        mRecyclerView.addOnScrollListener(mOnScrollListener);
        //mRecyclerView.setOnTouchListener(mPullToRefreshView);


    }

    private boolean mEnabled;
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);




        }
    };



}
