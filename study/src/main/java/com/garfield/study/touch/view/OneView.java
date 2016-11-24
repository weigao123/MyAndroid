package com.garfield.study.touch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //L.d("OneView onTouchEvent: "+event.getAction());
        boolean b = super.onTouchEvent(event);
        //L.d("OneView onTouchEvent: "+b);
        return true;
    }
}
