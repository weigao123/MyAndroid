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
        setEventListener();
    }

    private void setEventListener() {
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter.getItemEventListener() != null) {
                    mAdapter.getItemEventListener().onItemClick(mAdapter.getItems().get(mPosition));
                }
            }
        });
        mRootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mAdapter.getItemEventListener() != null) {
                    mAdapter.getItemEventListener().onItemLongPressed(mAdapter.getItems().get(mPosition));
                }
                return false;
            }
        });
    }

    /**
     * 内部调用
     */
    void refresh(int position) {
        mPosition = position;
        refresh((T)mAdapter.getItems().get(position));
    }

    protected abstract int getLayoutResId();

    protected abstract void inflateView();

    protected abstract void setView();

    protected abstract void refresh(T t);

    /**
     * 外部调用，刷新当前Holder
     */
    protected abstract void refresh();

    protected abstract TRecyclerAdapter getAdapter();

    protected <M extends View> M findView(int resId) {
        return (M) (mRootView.findViewById(resId));
    }

}  