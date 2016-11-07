package com.garfield.weishu.base.listview;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by gwball on 2016/9/27.
 */

public abstract class TListViewHolder<T> {

    protected View mRootView;
    protected TListAdapter mAdapter;
    protected int mPosition;

    /**
     * 入口
     */
    View bindViews(LayoutInflater inflater, TListAdapter adapter) {
        mRootView = inflater.inflate(getResId(), null);
        mAdapter = adapter;
        inflateView();
        setView();
        setEventListener();
        return mRootView;
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

    void refresh(T item, int position) {
        mPosition = position;
        refresh(item);
    }

    protected abstract int getResId();

    protected abstract void inflateView();

    protected abstract void setView();

    protected abstract void refresh(T item);

    protected abstract TListAdapter getAdapter();

    protected <M extends View> M findView(int resId) {
        return (M) (mRootView.findViewById(resId));
    }

    public boolean isFirstItem() {
        return mPosition == 0;
    }

    public boolean isLastItem() {
        return mPosition == mAdapter.getCount() - 1;
    }
}
