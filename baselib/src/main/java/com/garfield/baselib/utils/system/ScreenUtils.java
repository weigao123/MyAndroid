package com.garfield.baselib.utils.system;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import com.garfield.baselib.Cache;

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

    public static int statusBarHeight;
    public static int navBarHeight;

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
        statusBarHeight = getStatusBarHeight();
        navBarHeight = getNavBarHeight();
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

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = Cache.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = Cache.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getNavBarHeight(){
        Resources resources = Cache.getContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        // 不包括状态栏
        // 还可以直接拿到一个全屏的View(getRootView)，直接view.getWindowVisibleDisplayFrame(frame)
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    public static double getScreenPhysicalSize(Activity ctx) {
        double diagonalPixels = Math.sqrt(Math.pow(screenWidth, 2) + Math.pow(screenHeight, 2));
        return diagonalPixels / (160 * density);
    }

}
