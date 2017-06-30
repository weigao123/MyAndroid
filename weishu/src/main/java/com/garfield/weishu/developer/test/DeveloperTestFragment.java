package com.garfield.weishu.developer.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.garfield.weishu.R;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.base.recyclerview.BaseRecyclerAdapter;
import com.garfield.weishu.base.recyclerview.BaseRecyclerViewHolder;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by gaowei on 2017/5/23.
 */

public class DeveloperTestFragment extends AppBaseFragment implements BaseRecyclerAdapter.ItemEventListener<String> {

    @BindView(R.id.developer_test_grid_view)
    RecyclerView mRecyclerView;

    private List<String> mData = new ArrayList<>();

    @Override
    protected String onGetToolbarTitle() {
        return "Test";
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_developer_test;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);

        mData.add("ViewPager");
        mData.add("RecyclerView");
        mData.add("Binder");

        TestAdapter adapter = new TestAdapter(getContext(), mData);
        adapter.setItemEventListener(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(AppCache.getContext(), 3));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(String item, int position) {
        switch (item) {
            case "ViewPager":
                EventDispatcher.startFragmentEvent(new TestViewPagerFragment());
                break;
            case "Binder":
                EventDispatcher.startFragmentEvent(new TestBinderFragment());
                break;

        }

    }

    @Override
    public void onItemLongPressed(String item, int position) {

    }


    private class TestAdapter extends BaseRecyclerAdapter<String> {

        private TestAdapter(Context context, List<String> items) {
            super(context, items);
        }

        @Override
        public Class getViewHolderClassAtPosition(int position) {
            return RecyclerViewHolder.class;
        }
    }

    public static class RecyclerViewHolder extends BaseRecyclerViewHolder<String> {

        private TextView mTextView;

        @Override
        protected int getLayoutResId() {
            return R.layout.item_string;
        }

        @Override
        protected void inflateView() {
            mTextView = findView(R.id.item_string_text);
            mTextView.setGravity(Gravity.CENTER);
            ((ViewGroup)mRootView).getChildAt(1).setVisibility(View.GONE);
        }

        @Override
        protected void refresh(String s) {
            mTextView.setText(s);
        }
    }



}
