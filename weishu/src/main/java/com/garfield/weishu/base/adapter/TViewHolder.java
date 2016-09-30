package com.garfield.weishu.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by gwball on 2016/9/27.
 */

public abstract class TViewHolder {

    protected View mView;
    protected TAdapter mAdapter;

    protected int mPosition;
    protected Context mContext;

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

    protected void setContext(Context context) {
        mContext = context;
    }

    protected void setAdapter(TAdapter adapter) {
        this.mAdapter = adapter;
    }

    protected TAdapter getAdapter() {
        return this.mAdapter;
    }

    protected abstract int getResId();

    protected abstract void inflateChildView();

    protected abstract void refresh(Object item);

    protected <T extends View> T findView(int resId) {
        return (T) (mView.findViewById(resId));
    }

    public boolean isFirstItem() {
        return mPosition == 0;
    }

    public boolean isLastItem() {
        return mPosition == mAdapter.getCount() - 1;
    }
}
