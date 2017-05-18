package com.garfield.baselib.skinnable.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.garfield.baselib.skinnable.utils.AttrsHelper;

public class SkinnableLinearLayout extends LinearLayout implements Skinnable {
    private AttrsHelper mAttrsHelper;

    public SkinnableLinearLayout(Context context) {
        this(context, null);
    }

    public SkinnableLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinnableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
