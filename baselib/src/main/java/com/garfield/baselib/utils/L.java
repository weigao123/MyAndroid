package com.garfield.baselib.utils;

import android.util.Log;

/**
 * Created by gaowei3 on 2016/5/29.
 */
public class L {
    private static final String TAG = "gaowei";

    public static void d(String content) {
        Log.d(TAG, content);
    }

    public static void d(String TAG2, String content) {
        Log.d(TAG, TAG2 + ": " + content);
    }
}
