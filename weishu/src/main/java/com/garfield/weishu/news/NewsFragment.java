package com.garfield.weishu.news;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.garfield.weishu.R;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

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

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_new;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
    }
}
