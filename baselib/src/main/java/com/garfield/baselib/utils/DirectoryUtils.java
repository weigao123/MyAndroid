package com.garfield.baselib.utils;

import android.content.Context;
import android.os.Environment;

import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by gaowei3 on 2016/10/21.
 */

public class DirectoryUtils {

    public static File getOwnImageCacheDirectory(Context context) {
        return getOwnCacheDirectory(context, "/cache/image/");
    }

    public static File getOwnCacheDirectory(Context context, String cacheDir) {
        return getCacheDirectory(context, context.getPackageName() + cacheDir);
    }

    /**
     * 先使用外置(任意位置)，没有就使用内置(应用内部)
     */
    public static File getCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
        }
        if(appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
            appCacheDir = context.getCacheDir();
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == 0;
    }
}

