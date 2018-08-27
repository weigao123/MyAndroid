package com.garfield.baselib.skinnable.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.garfield.baselib.skinnable.utils.AttrsHelper;

/**
 * Created by gaowei on 2017/5/18.
 */

public class SkinnableView extends View implements Skinnable {

    private AttrsHelper mAttrsHelper;

    public SkinnableView(Context context) {
        super(context);
    }

    public SkinnableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinnableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAttrsHelper = new AttrsHelper();
        mAttrsHelper.obtainAttrs(context, attrs, defStyleAttr);
    }

    @Override
    public void applyDayNight() {
        mAttrsHelper.applySkin(getContext(), this);
    }
}
