package com.garfield.baselib.utils.system;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.ArrayMap;

import com.garfield.baselib.Cache;


/**
 * Created by gaowei on 2017/5/3.
 */

public class SharedPreferencesUtil {

    private static ArrayMap<String, SharedPreferences> mArrayMap = new ArrayMap<>();

    public static SharedPreferences with(Context context) {
        String fileName = context.getPackageName();
        if (mArrayMap.get(fileName) == null) {
            synchronized (SharedPreferencesUtil.class) {
                if (mArrayMap.get(fileName) == null) {
                    mArrayMap.put(fileName, context.getSharedPreferences(fileName, Context.MODE_PRIVATE));
                }
            }
        }
        return mArrayMap.get(fileName);
    }

    public static void saveInt(String key, int value) {
        with(Cache.getContext()).edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int def) {
        return with(Cache.getContext()).getInt(key, def);
    }

    public static void saveString(String key, String value) {
        with(Cache.getContext()).edit().putString(key, value).apply();
    }

    public static String getString(String key, String def) {
        return with(Cache.getContext()).getString(key, def);
    }

    public static void saveBoolean(String key, boolean value) {
        with(Cache.getContext()).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean def) {
        return with(Cache.getContext()).getBoolean(key, def);
    }
}
