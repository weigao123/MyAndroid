package com.garfield.study.ui.touch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static android.view.View.MeasureSpec.AT_MOST;

/**
 * Created by gaowei3 on 2016/8/16.
 */
public class OneView extends View {
    public OneView(Context context) {
        super(context);
    }

    public OneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OneView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);      //获取模式
        int width = MeasureSpec.getSize(widthMeasureSpec);        //获取尺寸
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == AT_MOST) {        //判断模式
            width = 200;
        }
        if (heightMode == AT_MOST) {
            height = 200;
        }
        setMeasuredDimension(width, height);    //最后进行设置
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //L.d("OneView onTouchEvent: "+event.getAction());
        boolean b = super.onTouchEvent(event);
        //L.d("OneView onTouchEvent: "+b);
        return true;
    }
}
