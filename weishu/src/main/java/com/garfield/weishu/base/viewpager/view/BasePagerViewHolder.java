package com.garfield.weishu.base.viewpager.view;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by gaowei3 on 2016/10/31.
 */

public abstract class BasePagerViewHolder<T> {
    protected View mRootView;
    protected BasePagerAdapter mAdapter;
    protected int mPosition;

    /**
     * 入口
     */
    View bindViews(LayoutInflater inflater, BasePagerAdapter adapter) {
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
                    mAdapter.getItemEventListener().onItemClick(mAdapter.getItems().get(mPosition), mPosition);
                }
            }
        });
        mRootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mAdapter.getItemEventListener() != null) {
                    mAdapter.getItemEventListener().onItemLongPressed(mAdapter.getItems().get(mPosition), mPosition);
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

    protected void setView() {

    }

    protected abstract void refresh(T item);

    protected BasePagerAdapter getAdapter() {
        return mAdapter;
    }

    @SuppressWarnings("unchecked")
    protected <M extends View> M findView(int resId) {
        return (M) (mRootView.findViewById(resId));
    }




}
