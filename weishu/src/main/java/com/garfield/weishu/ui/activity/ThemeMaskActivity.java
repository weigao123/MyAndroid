package com.garfield.weishu.ui.activity;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.garfield.baselib.utils.drawable.ColorUtils;
import com.garfield.baselib.utils.system.L;
import com.garfield.baselib.utils.system.ScreenUtils;
import com.garfield.baselib.utils.system.SystemUtil;
import com.garfield.weishu.R;

import java.lang.ref.WeakReference;

/**
 * Created by gaowei on 2017/5/8.
 */

public class ThemeMaskActivity extends AppCompatActivity implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

    private static Bitmap mBitmap;
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static WeakReference<Activity> mActivity;
    private static boolean mIsNightMode;
    private static int mColor = ColorUtils.getColor(R.color.color_303F9F);
    private static int mColorNight = ColorUtils.getColor(R.color.color_212B30);

    public static void start(final Activity activity, final boolean isNight) {
        mActivity = new WeakReference<>(activity);
        mIsNightMode = isNight;

        final ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        final View transView = new View(activity);
        transView.setClickable(true);
        rootView.addView(transView, -1, -1);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBitmap = getCacheBitmapFromView(activity.getWindow().getDecorView());
                if (mBitmap == null) {
                    rootView.removeView(transView);
                    return;
                }
                Intent intent = new Intent(activity, ThemeMaskActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        }, 50);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_mask);

        if (mIsNightMode) {
            SystemUtil.setStatusBarColorK(this, mColor);
        } else {
            SystemUtil.setStatusBarColorK(this, mColorNight);
        }

        final View rootView = findViewById(R.id.theme_mask);
        //noinspection deprecation
        rootView.setBackgroundDrawable(new BitmapDrawable(getResources(), mBitmap));

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.get().recreate();
                ViewPropertyAnimator animator =
                        rootView.animate()
                                .alpha(0f)
                                .setStartDelay(100)
                                .setDuration(300)
                                .setListener(ThemeMaskActivity.this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    animator.setUpdateListener(ThemeMaskActivity.this);
                }
                animator.start();
            }
        }, 100);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK;
    }

    private static Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            bitmap = cropBitmap(bitmap);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }

    private static Bitmap cropBitmap(Bitmap source) {
        int scaleFactor = 1;
        float statusBarHeight = ScreenUtils.statusBarHeight;
        Bitmap resizeBmp = Bitmap.createBitmap(
                source.getWidth() / scaleFactor,
                (int) (source.getHeight() - statusBarHeight) / scaleFactor,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resizeBmp);
        canvas.translate(0f, -statusBarHeight/scaleFactor);
        canvas.scale(1f/scaleFactor, 1f/scaleFactor);
        canvas.drawBitmap(source, 0, 0, new Paint());
        source.recycle();
        return resizeBmp;
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float fraction = animation.getAnimatedFraction();
        int color;
        if (mIsNightMode) {
            color = ColorUtils.evaluate(fraction, mColor, mColorNight);
        } else {
            color = ColorUtils.evaluate(fraction, mColorNight, mColor);
        }
        SystemUtil.setStatusBarColorK(this, color);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBitmap.recycle();
        mBitmap = null;
    }
}
