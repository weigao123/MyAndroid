package com.garfield.weishu.base.recyclerview;

import android.view.View;

/**
 * Adapter的所有item必须实现的接口
 * 不能作为内部类，否则newInstance不能用
 */
public abstract class BaseRecyclerViewHolder<T> {

    protected View mRootView;
    protected int mPosition;
    protected BaseRecyclerAdapter mAdapter;

    void bindViews(View rootView, BaseRecyclerAdapter adapter) {
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

    /**
     * 内部调用
     */
    void refresh(int position) {
        mPosition = position;
        refresh((T)mAdapter.getItems().get(position));
    }

    protected abstract int getLayoutResId();

    protected abstract void inflateView();

    protected void setView() {

    }

    protected abstract void refresh(T t);

    /**
     * 外部调用，根据当前的item值(前提是主动set更新过了)，刷新当前单个Holder
     */
    protected void refresh() {

    }

    @SuppressWarnings("unchecked")
    protected <M extends BaseRecyclerAdapter> M getAdapter() {
        return (M) mAdapter;
    }

    protected <M extends View> M findView(int resId) {
        return (M) (mRootView.findViewById(resId));
    }

}  