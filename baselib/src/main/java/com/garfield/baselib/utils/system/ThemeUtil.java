package com.garfield.baselib.utils.system;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatDelegate;
import android.util.TypedValue;

/**
 * Created by gaowei on 2017/5/9.
 */

public class ThemeUtil {

    public static int getThemeResId(Activity activity, int attrId) {
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(attrId, typedValue, true);
        return typedValue.data;
    }

    public static void setRightTheme() {
        if (SharedPreferencesUtil.getBoolean("night_mode")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
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
