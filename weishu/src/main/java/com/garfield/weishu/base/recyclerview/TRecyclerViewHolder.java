package com.garfield.weishu.base.recyclerview;

import android.view.View;

/**
 * Adapter的所有item必须实现的接口.<br>
 */
public abstract class TRecyclerViewHolder<T> {

    protected View mRootView;
    protected int mPosition;
    protected TRecyclerAdapter mAdapter;

    void bindViews(View rootView, TRecyclerAdapter adapter) {
        mRootView = rootView;
        mAdapter = adapter;
        inflateView();
        setView();
    }

    void refresh(T t, int position) {
        mPosition = position;
        refresh(t);
    }

    protected abstract int getLayoutResId();

    protected abstract void inflateView();

    protected abstract void setView();

    protected abstract void refresh(T t);

    protected abstract TRecyclerAdapter getAdapter();

    protected <M extends View> M findView(int resId) {
        return (M) (mRootView.findViewById(resId));
    }

}  