package com.garfield.baselib.utils.system;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.garfield.baselib.Cache;
import com.garfield.baselib.R;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES;

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

    /**
     * StatusBar
     */
    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarColorL(activity, color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setStatusBarColorK(activity, color);
        }
    }

    public static void setStatusBarColorK(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            /**
             * DecorView是一个FrameLayout，addView后会在最顶上，rootView使用FitsSystemWindows后，rootView会下移
             */
            View statusBarView = activity.findViewById(R.id.status_bar_id);
            if (statusBarView == null) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                statusBarView = new View(activity);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.statusBarHeight);
                statusBarView.setLayoutParams(params);
                ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
                decorView.addView(statusBarView);

                ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
                rootView.setFitsSystemWindows(true);    // 要和FLAG_TRANSLUCENT_STATUS一起使用
                rootView.setClipToPadding(true);
            }
            statusBarView.setBackgroundColor(color);
        }
    }

    private static void setStatusBarColorL(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /**
             * 一旦设置了FLAG_TRANSLUCENT_STATUS透明，就无效了
             */
            activity.getWindow().setStatusBarColor(color);
        }
    }

    /**
     * StatusBar，第三方库
     * https://github.com/jgilfelt/SystemBarTint
     * compile 'com.readystatesoftware.systembartint:systembartint:1.0.3'
     */
//    public static void setStatusBarColorTint(Activity activity, int color) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
//            rootView.setFitsSystemWindows(true);
//            rootView.setClipToPadding(true);
//
//            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintColor(color);
//        }
//    }

    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> info = packageManager.getInstalledPackages(GET_UNINSTALLED_PACKAGES);
        List<String> pName = new ArrayList<>();
        if (info != null) {
            for (int i = 0; i < info.size(); i++) {
                String pn = info.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
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
