package com.garfield.baselib.utils.system;

import android.util.Log;
import android.widget.Toast;

import com.garfield.baselib.Cache;

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

    public static void show(int resource) {
        Toast.makeText(Cache.getContext(), resource, Toast.LENGTH_SHORT).show();
    }

    public static void show(String string) {
        Toast.makeText(Cache.getContext(), string, Toast.LENGTH_SHORT).show();
    }
}
