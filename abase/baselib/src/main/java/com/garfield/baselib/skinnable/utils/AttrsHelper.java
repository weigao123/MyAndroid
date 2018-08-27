package com.garfield.baselib.skinnable.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.TextView;

import com.garfield.baselib.R;


public class AttrsHelper {

    private static final String DEFAULT_NS = "http://schemas.android.com/apk/res/android";
    private static final String CUSTOM_NS = "http://schemas.android.com/apk/res-auto";

    private SparseIntArray mResourceMap;

    private static final int VALUE_NOT_FOUND = -1;

    public AttrsHelper() {
        this.mResourceMap = new SparseIntArray();
    }

    public void obtainAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        //obtainLastAttrs(context, attrs, defStyleAttr);
        obtainFirstAttrs(attrs);
    }

    private void obtainLastAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        /**
         * 会自动遍历嵌套的资源，直到拿到真正的资源。。。
         */
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SkinnableBgAttr,
                defStyleAttr, 0);
        storeLastAttrs(a, R.styleable.SkinnableBgAttr);
        a.recycle();

        a = context.obtainStyledAttributes(attrs,
                R.styleable.SkinnableTextAttr,
                defStyleAttr, 0);
        storeLastAttrs(a, R.styleable.SkinnableTextAttr);
        a.recycle();
    }

    private void obtainFirstAttrs(AttributeSet attrs) {
        int bgResValue = attrs.getAttributeResourceValue(DEFAULT_NS, "background", VALUE_NOT_FOUND);
        int bgTintResValue = attrs.getAttributeResourceValue(DEFAULT_NS, "backgroundTint", VALUE_NOT_FOUND);
        int bgNightResValue = attrs.getAttributeResourceValue(CUSTOM_NS, "backgroundNight", VALUE_NOT_FOUND);
        int bgTintNightResValue = attrs.getAttributeResourceValue(CUSTOM_NS, "backgroundTintNight", VALUE_NOT_FOUND);

        int textResValue = attrs.getAttributeResourceValue(DEFAULT_NS, "textColor", VALUE_NOT_FOUND);
        int textNightResValue = attrs.getAttributeResourceValue(CUSTOM_NS, "textColorNight", VALUE_NOT_FOUND);

        /**
         * 系统自带的不同，其他的相同
         */
        //int key1 = R.styleable.SkinnableBgAttr[R.styleable.SkinnableBgAttr_android_background];
        //int key2 = R.attr.background;

        storeFirstAttrs(R.styleable.SkinnableBgAttr[R.styleable.SkinnableBgAttr_android_background], bgResValue);
        storeFirstAttrs(R.styleable.SkinnableBgAttr[R.styleable.SkinnableBgAttr_backgroundTint], bgTintResValue);
        storeFirstAttrs(R.attr.backgroundNight, bgNightResValue);
        storeFirstAttrs(R.attr.backgroundTintNight, bgTintNightResValue);

        storeFirstAttrs(R.styleable.SkinnableTextAttr[R.styleable.SkinnableTextAttr_android_textColor], textResValue);
        storeFirstAttrs(R.attr.textColorNight, textNightResValue);
    }

    private void storeFirstAttrs(int key, int value) {
        if (value != VALUE_NOT_FOUND) {
            mResourceMap.put(key, value);
        }
    }

    private void storeLastAttrs(TypedArray a, int[] styleable) {
        int size = a.getIndexCount();

        for (int index = 0; index < size; index ++) {
            int resourceId = a.getResourceId(a.getIndex(index), VALUE_NOT_FOUND);
            int key = styleable[a.getIndex(index)];
            if (resourceId != VALUE_NOT_FOUND) {
                mResourceMap.put(key, resourceId);
            }
        }

    }

    private int getAttributeResource(int attr) {
        return mResourceMap.get(attr, VALUE_NOT_FOUND);
    }

    public void applySkin(Context context, View view) {
        /**
         * 如果isNativeNightModeEnable=true，直接在不同的文件夹放同名颜色，就不需要使用Night属性了
         */
        int key;

        key = R.styleable.SkinnableBgAttr[!ThemeUtil.isNightMode() || ThemeUtil.isNativeNightModeEnable() ?
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
