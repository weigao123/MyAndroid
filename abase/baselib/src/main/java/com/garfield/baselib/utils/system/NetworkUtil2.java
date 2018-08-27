package com.garfield.baselib.utils.system;

import android.content.Context;

/**
 * Created by gaowei on 2017/5/19.
 */

public class NetworkUtil2 {

    /**
     * 检测当前网络能否访问远程服务器(国内通过ping百度来检测)
     * @param context
     * @return
     */
    public static boolean isNetWorkAvailable(final Context context) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process pingProcess = runtime.exec("/system/bin/ping -c 1 www.baidu.com");
            int exitCode = pingProcess.waitFor(); //0 代表连通，2代表不通
            return (exitCode == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
