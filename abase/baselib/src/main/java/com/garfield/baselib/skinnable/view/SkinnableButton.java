package com.garfield.baselib.skinnable.view;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.garfield.baselib.skinnable.utils.AttrsHelper;


public class SkinnableButton extends AppCompatButton implements Skinnable {

    private AttrsHelper mAttrsHelper;

    public SkinnableButton(Context context) {
        this(context, null);
    }

    public SkinnableButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }

    public SkinnableButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
