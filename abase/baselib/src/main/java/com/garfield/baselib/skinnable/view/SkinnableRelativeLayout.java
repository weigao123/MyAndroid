package com.garfield.baselib.skinnable.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.garfield.baselib.skinnable.utils.AttrsHelper;


public class SkinnableRelativeLayout extends RelativeLayout implements Skinnable {
    private AttrsHelper mAttrsHelper;

    public SkinnableRelativeLayout(Context context) {
        this(context, null);
    }

    public SkinnableRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinnableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
