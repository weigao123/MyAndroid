package com.garfield.baselib.skinnable.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.garfield.baselib.R;
import com.garfield.baselib.skinnable.view.Skinnable;


public class AttrsHelper {

    private SparseIntArray mResourceMap;

    private static final int VALUE_NOT_FOUND = -1;

    public AttrsHelper() {
        this.mResourceMap = new SparseIntArray();
    }

    public void storeAttributeResource(TypedArray a, int[] styleable) {
        int size = a.getIndexCount();
        for (int index = 0; index < size; index ++) {
            int resourceId = a.getResourceId(index, VALUE_NOT_FOUND);
            int key = styleable[index];
            if (resourceId != VALUE_NOT_FOUND) {
                mResourceMap.put(key, resourceId);
            }
        }
    }

    public int getAttributeResource(int attr) {
        return mResourceMap.get(attr, VALUE_NOT_FOUND);
    }

    public void obtain(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SkinnableBgAttr,
                defStyleAttr, 0);
        storeAttributeResource(a, R.styleable.SkinnableBgAttr);
        a.recycle();

        a = context.obtainStyledAttributes(attrs,
                R.styleable.SkinnableTextAttr,
                defStyleAttr, 0);
        storeAttributeResource(a, R.styleable.SkinnableTextAttr);
        a.recycle();

    }

    public void applySkin(Context context, View view) {

        int key;

        key = R.styleable.SkinnableBgAttr[true ?
                R.styleable.SkinnableBgAttr_android_background :
                R.styleable.SkinnableBgAttr_backgroundNight];
        int backgroundResource = getAttributeResource(key);
        if (backgroundResource > 0) {
            Drawable background = ContextCompat.getDrawable(context, backgroundResource);
            //noinspection deprecation
            view.setBackgroundDrawable(background);
        }

        if (view instanceof AppCompatTextView) {
            key = R.styleable.SkinnableBgAttr[!ThemeUtil.isNightMode() || ThemeUtil.isNativeNightModeEnable() ?
                    R.styleable.SkinnableBgAttr_backgroundTint :
                    R.styleable.SkinnableBgAttr_backgroundTintNight];
            int backgroundTintResource = getAttributeResource(key);
            if (backgroundTintResource > 0) {
                ColorStateList backgroundTint = ContextCompat.getColorStateList(context, backgroundTintResource);
                ((AppCompatTextView) view).setSupportBackgroundTintList(backgroundTint);
            }
        }

        if (view instanceof TextView) {
            key = R.styleable.SkinnableTextAttr[!ThemeUtil.isNightMode() || ThemeUtil.isNativeNightModeEnable() ?
                    R.styleable.SkinnableTextAttr_android_textColor :
                    R.styleable.SkinnableTextAttr_textColorNight];
            int textColorResource = getAttributeResource(key);
            if (textColorResource > 0) {
                ColorStateList color = ContextCompat.getColorStateList(context, textColorResource);
                ((TextView) view).setTextColor(color);
            }
        }
    }

}
