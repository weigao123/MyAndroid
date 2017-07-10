package com.garfield.study.ui.touch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.garfield.baselib.utils.system.L;

/**
 * Created by gaowei3 on 2016/12/9.
 */

public class MyLinearLayout extends LinearLayout {
    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);      //获取模式
        int width = MeasureSpec.getSize(widthMeasureSpec);        //获取尺寸
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        L.d("widthMode: "+widthMode);
        L.d("width: "+width);
        L.d("heightMode: "+heightMode);
        L.d("height: "+height);

        L.d("width2: "+getMeasuredWidth());
        L.d("height2: "+getMeasuredHeight());

    }
}
