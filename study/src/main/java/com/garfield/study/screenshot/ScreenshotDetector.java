package com.garfield.study.screenshot;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import com.garfield.baselib.utils.system.ScreenUtil;
/**
 * Created by liqiang1 on 2017/8/29.
 */

public class ScreenshotDetector {

    private static final String TAG = ScreenshotDetector.class.getSimpleName();

    // 匹配uri
    private static final String EXTERNAL_CONTENT_URI_MATCHER = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString();
    // 降序排列
    private static final String SORT_ORDER = MediaStore.Images.Media.DATE_ADDED + " DESC";
    // 查询返回的列
    private static final String[] PROJECTION = new String[]{
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_ADDED
    };
    // 文件生成时间过滤间隔s
    private static final long DEFAULT_DETECT_WINDOW_SECONDS = 5;
    // 文件路径过滤关键字
    private static final String[] KEYWORDS = new String[]{
            "screenshot", "screenshots", "screen_shot", "screen-shot", "screen shot",
            "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap", "截屏", "截图"
    };

    private static ScreenshotDetector sDetector;

    private Activity mActivity;

    private Context mContext;

    private ContentResolver mContentResolver;

    private String mCurrentActivity;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private final ContentObserver contentObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange, Uri uri) {

            // 过滤重复截屏行为
//            if (TextUtils.equals(ScreenshotActivity.class.getSimpleName(), mCurrentActivity)) {
//                return;
//            }
            Log.d(TAG, "onChange: " + selfChange + ", " + uri.toString());
            if (uri.toString().matches(EXTERNAL_CONTENT_URI_MATCHER) || uri.toString().contains(EXTERNAL_CONTENT_URI_MATCHER)) {
                Cursor cursor = null;
                try {
                    cursor = mContentResolver.query(uri, PROJECTION, null, null, SORT_ORDER);
                    if (cursor != null && cursor.moveToFirst()) {
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        long dateAdded = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                        Log.d(TAG, "path: " + path);
                        if (checkScreenshot(path, dateAdded)) {
                            // screenshot added!
                            Log.d(TAG, "screenshot added!");
                        }
                    }
                } catch (Exception e) {
                    Log.d(TAG, "open cursor fail");
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
            super.onChange(selfChange, uri);
        }
    };

    public static synchronized ScreenshotDetector getInstance(Context context) {
        if (sDetector == null) {
            sDetector = new ScreenshotDetector(context);
        }
        return sDetector;
    }

    public ScreenshotDetector(Context context) {
        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    public void onGotoForeground(Activity activity) {
        Log.d(TAG, "onGotoForeground");
        try {
            mActivity = activity;
            mContentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, contentObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onGotoBackground() {
        Log.d(TAG, "onGotoBackground");
        try {
            mActivity = null;
            mHandler.removeCallbacksAndMessages(null);
            mContentResolver.unregisterContentObserver(contentObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验截图(时间/关键词/尺寸)
     *
     * @param path
     * @param addedTime
     * @return
     */
    private boolean checkScreenshot(String path, long addedTime) {
        long currentTime = System.currentTimeMillis() / 1000;
        if (Math.abs(currentTime - addedTime) > DEFAULT_DETECT_WINDOW_SECONDS) {
            return false;
        }
        // 需要等一下,否则部分手机立即通过文件路径获取bitmap获取不到,和机器性能有关,无法确定固定值
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Bitmap shotBitmap = BitmapFactory.decodeFile(path);
        int orientation = mContext.getResources().getConfiguration().orientation;
        int height = shotBitmap.getHeight();
        int width = shotBitmap.getWidth();
        Log.d(TAG, "screenWidth:" + ScreenUtil.screenWidth);
        Log.d(TAG, "screenHeight:" + ScreenUtil.screenHeight);
        Log.d(TAG, "shotWidth:" + width);
        Log.d(TAG, "shotHeight:" + height);
        Log.d(TAG, "orientation:" + orientation);
        shotBitmap.recycle();

        // 过滤掉横屏截图
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        }

        for (String key : KEYWORDS) {
            if (path.toLowerCase().contains(key) && !path.contains(SaveFileUtil.FILE_HEAD)) {
                return true;
            }
        }
        return false;
    }



}
