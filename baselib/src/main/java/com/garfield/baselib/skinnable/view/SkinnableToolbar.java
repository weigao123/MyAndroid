package com.garfield.baselib.skinnable.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.garfield.baselib.R;
import com.garfield.baselib.skinnable.utils.AttrsHelper;


public class SkinnableToolbar extends Toolbar implements Skinnable {

    private AttrsHelper mAttrsHelper;

    public SkinnableToolbar(Context context) {
        this(context, null);
    }

    public SkinnableToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.toolbarStyle);
    }

    public SkinnableToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mAttrsHelper = new AttrsHelper();

        TypedArray a;

        a = context.obtainStyledAttributes(attrs,
                R.styleable.SkinnableBgAttr,
                defStyleAttr, 0);
        mAttrsHelper.storeAttributeResource(a, R.styleable.SkinnableBgAttr);
        a.recycle();

        a = context.obtainStyledAttributes(attrs,
                R.styleable.Toolbar,
                defStyleAttr, 0);
        mAttrsHelper.storeAttributeResource(a, R.styleable.Toolbar);
        a.recycle();
    }

    @Override
    public void applyDayNight() {
        Context context = getContext();
        int key;

        key = R.styleable.SkinnableBgAttr[R.styleable.SkinnableBgAttr_android_background];
        int backgroundResource = mAttrsHelper.getAttributeResource(key);
        if (backgroundResource > 0) {
            Drawable background = ContextCompat.getDrawable(context, backgroundResource);
            setBackgroundDrawable(background);

        }

        key = R.styleable.Toolbar[R.styleable.Toolbar_titleTextAppearance];
        int titleTextAppearance = mAttrsHelper.getAttributeResource(key);
        if (titleTextAppearance > 0) {
            setTitleTextAppearance(context, titleTextAppearance);
        }

        key = R.styleable.Toolbar[R.styleable.Toolbar_subtitleTextAppearance];
        int subTitleTextAppearance = mAttrsHelper.getAttributeResource(key);
        if (titleTextAppearance > 0) {
            setSubtitleTextAppearance(context, subTitleTextAppearance);
        }
    }

}
