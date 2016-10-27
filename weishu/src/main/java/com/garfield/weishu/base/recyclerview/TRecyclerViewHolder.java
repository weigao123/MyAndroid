package com.garfield.weishu.base.recyclerview;

import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * Adapter的所有item必须实现的接口.<br>
 */
public abstract class TRecyclerViewHolder<T> {

    protected View mRootView;
    private int mPosition;

    public abstract int getLayoutResId();


    public void bindViews(View rootView) {
        mRootView = rootView;
        inflateChildView();
    }

    protected abstract void inflateChildView();

    public abstract void refresh(T t, int position);

    protected <M extends View> M findView(int resId) {
        return (M) (mRootView.findViewById(resId));
    }

}  