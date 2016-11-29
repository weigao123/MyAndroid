package com.garfield.baselib.utils.drawable;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.garfield.baselib.Cache;

/**
 * Created by gaowei3 on 2016/5/26.
 */
public class ScreenSizeUtils {

    public static int dp2px(float var1) {
        float var2 = Cache.getContext().getResources().getDisplayMetrics().density;
        return (int)(var1 * var2 + 0.5F);

        /**
         * 第一个参数是要转化的参数的单位dp
         * 第二个参数是要被转换的数，以第一个参数为单位
         */
        //TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, getResources().getDisplayMetrics());
    }

    public static int px2dp(float var1) {
        float var2 = Cache.getContext().getResources().getDisplayMetrics().density;
        return (int)(var1 / var2 + 0.5F);
    }

    public static int sp2px(float var1) {
        float var2 = Cache.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int)(var1 * var2 + 0.5F);
    }

    public static int px2sp(float var1) {
        float var2 = Cache.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int)(var1 / var2 + 0.5F);
    }

    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        // 不包括状态栏
        // 还可以直接拿到一个全屏的View(getRootView)，直接view.getWindowVisibleDisplayFrame(frame)
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    public static Point getScreenSize() {
        WindowManager windowManager = (WindowManager) Cache.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return new Point(metrics.widthPixels, metrics.heightPixels);
    }

    public static int getScreenWidth() {
        WindowManager windowManager = (WindowManager) Cache.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getScreenHeight() {
        WindowManager windowManager = (WindowManager) Cache.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static float getScreenScale() {
        WindowManager windowManager = (WindowManager) Cache.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels > metrics.heightPixels ?
                (float) metrics.widthPixels / metrics.heightPixels :
                (float) metrics.heightPixels / metrics.widthPixels;
    }

}
