package com.garfield.weishu.discovery.developer;

import android.widget.TextView;

import com.garfield.weishu.R;
import com.garfield.weishu.base.recyclerview.TRecyclerAdapter;
import com.garfield.weishu.base.recyclerview.TRecyclerViewHolder;

/**
 * Created by gaowei3 on 2016/11/30.
 */

public class DevelopListViewHolder extends TRecyclerViewHolder<String> {

    private TextView mTextView;
    @Override
    protected int getLayoutResId() {
        return R.layout.item_string;
    }

    @Override
    protected void inflateView() {
        mTextView = findView(R.id.item_string_text);
    }

    @Override
    protected void setView() {

    }

    @Override
    protected void refresh(String s) {
        mTextView.setText(s);
    }

    @Override
    protected void refresh() {

    }

    @Override
    protected TRecyclerAdapter getAdapter() {
        return null;
    }
}
