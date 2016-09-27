package com.garfield.weishu.base;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by gwball on 2016/9/27.
 */

public abstract class BaseHolder {

    protected View mView;

    public View getView(LayoutInflater inflater) {
        int resId = getResId();
        mView = inflater.inflate(resId, null);
        inflate();
        return mView;
    }

    protected abstract int getResId();

    protected abstract void inflate();

    protected abstract void refresh(Object item);

    protected <T extends View> T findView(int resId) {
        return (T) (mView.findViewById(resId));
    }
}
