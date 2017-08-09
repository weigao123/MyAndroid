package com.garfield.weishu.developer.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.garfield.baselib.utils.array.ArrayUtils;
import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.weishu.base.recyclerview.BaseRecyclerAdapter;
import com.garfield.weishu.base.recyclerview.BaseRecyclerViewHolder;
import com.garfield.weishu.base.recyclerview.RecyclerViewHolder;
import com.garfield.weishu.developer.test.NonUI.HolderBean;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by gaowei on 2017/8/9.
 */

public class TestRecyclerViewFragment extends AppBaseFragment {

    @BindView(R.id.test_recyclerview)
    RecyclerView mRootRecyclerView;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_test_recyclerview;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        List<HolderBean> data = new ArrayList<>(Arrays.asList(ArrayUtils.create(HolderBean.class, 50)));
        RootAdapter adapter = new RootAdapter(getContext(), data);
        mRootRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRootRecyclerView.setAdapter(adapter);
    }

    private class RootAdapter extends BaseRecyclerAdapter<HolderBean> {
        private RootAdapter(Context context, List<HolderBean> items) {
            super(context, items);
        }

        @Override
        public Class getViewHolderClassAtPosition(int position) {
            return NormalViewHolder.class;
        }

        @Override
        public void onViewAttachedToWindow(RecyclerViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            L.d("onViewAttachedToWindow position: "+holder.getTViewHolder().mPosition);
        }

        @Override
        public void onViewDetachedFromWindow(RecyclerViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            L.d("onViewDetachedFromWindow position: "+holder.getTViewHolder().mPosition);
        }
    }

    public static class NormalViewHolder extends BaseRecyclerViewHolder<HolderBean> {

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
        protected void refresh(HolderBean s) {
            L.d("refresh: " + s);
            mTextView.setText(s.toString());
        }
    }


}
