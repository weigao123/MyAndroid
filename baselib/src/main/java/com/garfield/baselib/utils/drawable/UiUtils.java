package com.garfield.baselib.utils.drawable;

/**
 * Created by gaowei3 on 2016/11/24.
 */

public class UiUtils {

    private static long lastClickTime;

    private static final int DEFAULT = 500;

    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(DEFAULT);
    }

    public static boolean isFastDoubleClick(int diff) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < diff) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
