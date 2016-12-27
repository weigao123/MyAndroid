package com.garfield.baselib.utils.system;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.garfield.baselib.Cache;

import java.lang.reflect.Field;

/**
 * Created by gaowei3 on 2016/5/26.
 */
public class ScreenUtils {

    public static int screenWidth;
    public static int screenHeight;
    public static int screenMin;// 宽高中，小的一边
    public static int screenMax;// 宽高中，较大的值
    public static float screenScale;

    public static float density;
    public static float scaleDensity;
    public static float xdpi;
    public static float ydpi;
    public static int densityDpi;

    public static int statusbarheight;
    public static int navbarheight;

    static {
        init(Cache.getContext());
    }

    private static void init(Context context) {
        if (null == context) {
            return;
        }
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        screenMin = (screenWidth > screenHeight) ? screenHeight : screenWidth;
        screenMax = (screenWidth < screenHeight) ? screenHeight : screenWidth;
        screenScale = (float) screenMax / screenMin;

        density = dm.density;
        scaleDensity = dm.scaledDensity;
        xdpi = dm.xdpi;
        ydpi = dm.ydpi;
        densityDpi = dm.densityDpi;
        statusbarheight = getStatusBarHeight();
        navbarheight = getNavBarHeight();
    }

    public static int dp2px(float dpValue) {
        return (int) (dpValue * density + 0.5f);
        /**
         * 第一个参数是要转化的参数的单位dp
         * 第二个参数是要被转换的数，以第一个参数为单位
         */
        //TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, getResources().getDisplayMetrics());
    }

    public static int px2dp(float pxValue) {
        return (int) (pxValue / density + 0.5f);
    }

    public static int sp2px(float spValue) {
        return (int) (spValue * scaleDensity + 0.5F);
    }

    public static int px2sp(float pxValue) {
        return (int) (pxValue / scaleDensity + 0.5F);
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



    public static int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = Cache.getContext().getResources().getDimensionPixelSize(x);
        } catch (Exception E) {
            E.printStackTrace();
        }
        return sbar;
    }

    public static int getNavBarHeight(){
        Resources resources = Cache.getContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static double getScreenPhysicalSize(Activity ctx) {
        double diagonalPixels = Math.sqrt(Math.pow(screenWidth, 2) + Math.pow(screenHeight, 2));
        return diagonalPixels / (160 * density);
    }

}
