package com.garfield.weishu.session.session;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.garfield.baselib.utils.system.L;

/**
 * Created by gaowei on 2017/5/3.
 */

public class KeyboardLinearLayout extends LinearLayout {

    private OnMeasureListener mListener;

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
            mListener.onMeasure(height);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void registerMeasureListener(OnMeasureListener listener) {
        mListener = listener;
    }

    public interface OnMeasureListener {
        void onMeasure(int nowHeight);
    }
}
