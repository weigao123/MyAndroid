package com.garfield.baselib.utils.system;

import android.util.Log;
import android.widget.Toast;

import com.garfield.baselib.Cache;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;


/**
 * Created by gaowei3 on 2016/5/29.
 */
public class L {

    private static final String TAG = "gaowei";

    static {
        Logger.init(TAG)                 // default PRETTYLOGGER or use just init()
            .methodCount(0)                 // default 2
            .hideThreadInfo()               // default shown
            .logLevel(LogLevel.FULL)        // default LogLevel.FULL
            .methodOffset(1);                // default 0
            //.logAdapter(new AndroidLogAdapter()); //default AndroidLogAdapter
    }

    public static void d(Object content) {
        //Log.d(TAG, content);
        if (content instanceof Throwable) {
            Log.d(TAG, Log.getStackTraceString((Throwable)content));
        } else {
            Log.d(TAG, content.toString());
        }
    }

    public static void d(String TAG, Object content) {
        Log.d(TAG, content.toString());
        //Logger.t(TAG).d(content);
    }

    public static void json(String json) {
        Logger.json(json);
    }

    public static void toast(int resource) {
        Toast.makeText(Cache.getContext(), resource, Toast.LENGTH_SHORT).show();
    }

    public static void toast(String string) {
        Toast.makeText(Cache.getContext(), string, Toast.LENGTH_SHORT).show();
    }
}
