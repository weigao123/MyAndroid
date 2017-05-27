package com.garfield.weishu.developer.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by gaowei on 2017/5/26.
 */

public class TouchableFrameLayout extends FrameLayout {

    public TouchableFrameLayout(@NonNull Context context) {
        super(context);
    }

    public TouchableFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchableFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        View view = getChildAt(0);
        return view.onTouchEvent(event);
    }
}
