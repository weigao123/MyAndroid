package com.garfield.weishu.developer.test;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.garfield.baselib.utils.array.ArrayUtils;
import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.weishu.base.listview.BaseListAdapter;
import com.garfield.weishu.base.listview.BaseListViewHolder;
import com.garfield.weishu.developer.test.NonUI.HolderBean;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by gaowei on 2017/8/10.
 */

public class TestListViewFragment extends AppBaseFragment {

    @BindView(R.id.test_listview)
    ListView mListView;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_test_listview;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        List<HolderBean> data = new ArrayList<>(Arrays.asList(ArrayUtils.create(HolderBean.class, 50)));
        RootAdapter adapter = new RootAdapter(getContext(), data);
        mListView.setAdapter(adapter);
    }

    public class RootAdapter extends BaseListAdapter<HolderBean> {

        public RootAdapter(Context context, List<HolderBean> items) {
            super(context, items);
        }

        @Override
        public Class getViewHolderClassAtPosition(int position) {
            return NormalViewHolder.class;
        }
    }

    public static class NormalViewHolder extends BaseListViewHolder<HolderBean> {

        private TextView mTextView;

        @Override
        protected int getResId() {
            return R.layout.item_string;
        }

        @Override
        protected void inflateView() {
            mTextView = findView(R.id.item_string_text);
            mTextView.setGravity(Gravity.CENTER);
            ((ViewGroup)mRootView).getChildAt(1).setVisibility(View.GONE);
        }

        @Override
        protected void refresh(HolderBean item) {
            L.d("refresh: " + item);
            mTextView.setText(item.toString());
        }
    }


}
