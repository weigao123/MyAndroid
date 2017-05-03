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

    public static boolean saveInt(String key, int value) {
        return with(Cache.getContext()).edit()
                .putInt(key, value)
                .commit();
    }

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

    public static int getInt(String key) {
        return with(Cache.getContext()).getInt(key, 0);
    }
}
