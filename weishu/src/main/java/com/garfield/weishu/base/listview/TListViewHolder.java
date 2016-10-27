package com.garfield.weishu.base.listview;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by gwball on 2016/9/27.
 */

public abstract class TListViewHolder<T> {

    protected View mView;
    protected TListAdapter mAdapter;

    protected int mPosition;

    /**
     * 入口
     */
    public View getView(LayoutInflater inflater) {
        int resId = getResId();
        mView = inflater.inflate(resId, null);
        inflateChildView();
        return mView;
    }

    protected void setPosition(int position) {
        this.mPosition = position;
    }

    protected void setAdapter(TListAdapter adapter) {
        this.mAdapter = adapter;
    }

    protected TListAdapter getAdapter() {
        return this.mAdapter;
    }

    protected abstract int getResId();

    protected abstract void inflateChildView();

    protected abstract void refresh(T item);

    protected <M extends View> M findView(int resId) {
        return (M) (mView.findViewById(resId));
    }

    public boolean isFirstItem() {
        return mPosition == 0;
    }

    public boolean isLastItem() {
        return mPosition == mAdapter.getCount() - 1;
    }
}
