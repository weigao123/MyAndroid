package com.garfield.baselib.skinnable.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.garfield.baselib.skinnable.utils.AttrsHelper;

public class SkinnableTextView extends AppCompatTextView implements Skinnable {

    private AttrsHelper mAttrsHelper;

    public SkinnableTextView(Context context) {
        this(context, null);
    }

    public SkinnableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public SkinnableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
