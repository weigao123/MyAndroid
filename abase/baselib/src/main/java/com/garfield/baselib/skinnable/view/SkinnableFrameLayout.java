package com.garfield.baselib.skinnable.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.garfield.baselib.skinnable.utils.AttrsHelper;


public class SkinnableFrameLayout extends FrameLayout implements Skinnable {

    private AttrsHelper mAttrsHelper;

    public SkinnableFrameLayout(Context context) {
        this(context, null);
    }

    public SkinnableFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinnableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAttrsHelper = new AttrsHelper();
        mAttrsHelper.obtainAttrs(context, attrs, defStyleAttr);

        applyDayNight();
    }

    @Override
    public void applyDayNight() {
        mAttrsHelper.applySkin(getContext(), this);
    }

}
