package com.garfield.weishu.session.session;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by gaowei on 2017/5/3.
 */

public class KeyboardLinearLayout extends LinearLayout {

    private OnMeasureListener mListener;
    private int mOldHeight;

    public KeyboardLinearLayout(Context context) {
        super(context);
    }

    public KeyboardLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (mListener != null) {
            mListener.onMeasureBefore(mOldHeight, height);
        }
        mOldHeight = height;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void registerMeasureListener(OnMeasureListener listener) {
        mListener = listener;
    }

    public interface OnMeasureListener {
        void onMeasureBefore(int oldHeight, int newHeight);
    }
}
