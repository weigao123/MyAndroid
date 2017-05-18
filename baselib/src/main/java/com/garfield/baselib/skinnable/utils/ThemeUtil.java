package com.garfield.baselib.skinnable.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatDelegate;
import android.util.TypedValue;

import com.garfield.baselib.utils.system.SharedPreferencesUtil;

/**
 * Created by gaowei on 2017/5/9.
 */

public class ThemeUtil {

    private static final int DAY_MODE = 1;
    private static final int NIGHT_MODE = 2;
    private static int mMode = 0;

    /**
     * 要在-night文件夹下放入资源
     *
     * 否则就在layout文件里多定义一套资源
     */
    private static boolean mIsNativeNightModeEnable = true;

    public static void setNativeNightModeEnable(boolean enable) {
        mIsNativeNightModeEnable = enable;
    }

    public static boolean isNativeNightModeEnable() {
        return mIsNativeNightModeEnable;
    }


    public static int getThemeResId(Activity activity, int attrId) {
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(attrId, typedValue, true);
        return typedValue.data;
    }

    public static void setLastNativeNightMode() {
        if (isNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static void saveNightMode(boolean nightMode) {
        mMode = nightMode ? NIGHT_MODE : DAY_MODE;
        SharedPreferencesUtil.saveInt("night_mode", mMode);
    }

    public static boolean isNightMode() {
        if (mMode == 0) {
            mMode = SharedPreferencesUtil.getInt("night_mode", DAY_MODE);
        }
        return mMode == NIGHT_MODE;
    }

    public static int getColorFromAttrRes(int attr, Context context) {
        TypedArray a = context.obtainStyledAttributes(new int[] {attr});
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }

    public static float getFloatFromAttrRes(int attrRes, Context context) {
        TypedArray a = context.obtainStyledAttributes(new int[] {attrRes});
        try {
            return a.getFloat(0, 0);
        } finally {
            a.recycle();
        }
    }
}
