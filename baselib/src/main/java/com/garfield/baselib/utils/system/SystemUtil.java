package com.garfield.baselib.utils.system;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Debug;
import android.text.TextUtils;

import com.garfield.baselib.Cache;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * 系统工具箱
 */
public class SystemUtil {

    public static boolean inMainProcess() {
        String packageName = Cache.getContext().getPackageName();
        String processName = getProcessName();
        return packageName.equals(processName);
    }

    /**
     * 获取当前进程名
     */
    public static String getProcessName() {
        ActivityManager am = (ActivityManager) Cache.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == android.os.Process.myPid()) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static void setStatusColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printMemInfo() {
        ActivityManager activityManager = (ActivityManager)  Cache.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(mi);
        L.d("mi.availMem  " + mi.availMem/1024/1024f);
        L.d("mi.threshold  " + mi.threshold/1024/1024f);
        L.d("mi.lowMemory  " + mi.lowMemory);
        L.d("getMemoryClass  " + activityManager.getMemoryClass());

        Runtime runtime = Runtime.getRuntime();
        L.d("runtime.maxMemory  " + runtime.maxMemory()/1024/1024f);
        L.d("runtime.totalMemory  " + runtime.totalMemory()/1024/1024f);
        L.d("runtime.freeMemory  " + runtime.freeMemory()/1024/1024f);
    }

}
