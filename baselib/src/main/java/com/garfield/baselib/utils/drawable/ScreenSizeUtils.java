package com.garfield.baselib.utils.drawable;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by gaowei3 on 2016/5/26.
 */
public class ScreenSizeUtils {

    public static int dp2px(Context context, float var1) {
        float var2 = context.getResources().getDisplayMetrics().density;
        return (int)(var1 * var2 + 0.5F);

        /**
         * 第一个参数是要转化的参数的单位dp
         * 第二个参数是要被转换的数，以第一个参数为单位
         */
        //TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, getResources().getDisplayMetrics());
    }

    public static int px2dp(Context context, float var1) {
        float var2 = context.getResources().getDisplayMetrics().density;
        return (int)(var1 / var2 + 0.5F);
    }

    public static int sp2px(Context context, float var1) {
        float var2 = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(var1 * var2 + 0.5F);
    }

    public static int px2sp(Context context, float var1) {
        float var2 = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(var1 / var2 + 0.5F);
    }

    public static int getStatusBarHeight(Context context) {
        if (context instanceof Activity) {
            Rect frame = new Rect();
            // 不包括状态栏
            // 还可以直接拿到一个全屏的View(getRootView)，直接view.getWindowVisibleDisplayFrame(frame)
            ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            return frame.top;
        }
        return -1;
    }

    public static Point getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return new Point(metrics.widthPixels, metrics.heightPixels);
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }

}
