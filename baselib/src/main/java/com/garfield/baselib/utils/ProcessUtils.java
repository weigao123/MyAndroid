package com.garfield.baselib.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

/**
 * Created by gwball on 2016/8/30.
 */
public class ProcessUtils {

    public static boolean inMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = getProcessName(context);
        return packageName.equals(processName);
    }
    /**
     * 获取当前进程名
     * @param context
     * @return 进程名
     */
    public static String getProcessName(Context context) {
        String processName = null;
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    break;
                }
            }
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
