package com.garfield.study.touch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.garfield.baselib.utils.L;

/**
 * Created by gaowei3 on 2016/8/16.
 */
public class OneViewGroup extends LinearLayout {
    public OneViewGroup(Context context) {
        super(context);
    }

    public OneViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OneViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        L.d("OneViewGroup onInterceptTouchEvent: "+ev.getAction());
//        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//            L.d("OneViewGroup true");
//            return true;
//        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        L.d("OneViewGroup onTouchEvent: "+event.getAction());

        return false;
    }
}
