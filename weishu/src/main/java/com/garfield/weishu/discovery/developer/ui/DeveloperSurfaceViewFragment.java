package com.garfield.weishu.discovery.developer.ui;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.garfield.baselib.utils.system.L;
import com.garfield.baselib.utils.system.ScreenUtils;
import com.garfield.weishu.R;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaowei3 on 2017/1/12.
 */

public class DeveloperSurfaceViewFragment extends AppBaseFragment implements SurfaceHolder.Callback {

    private static final int BALL_RADIUS = ScreenUtils.dp2px(10);

    @BindView(R.id.fragment_developer_surfaceview)
    SurfaceView mSurfaceView;

    private float mRealWidth;
    private float mRealHeight;
    private List<PointF> mPointFs = new ArrayList<>();

    /**
     * SurfaceHolder 是对 SurfaceView 的 Surface 的包装
     */
    private SurfaceHolder mSurfaceHolder;
    private volatile boolean mCanDraw;

    private ValueAnimator mAnimator;
    private PointF mPointF = new PointF(0, 0);
    private Paint ballPaint;
    private Paint linePaint;
    private Paint clearPaint;
    private volatile boolean mIsChanged = true;

    /**
     * 原理是x^2只有开始那一段(0~k)坡度较缓，同步放大即可
     * k是SurfaceView的斜率，也是y=kx和y=x^2的交点
     * multiple是k到Width被放大的倍数
     */
    private double k;
    private double multiple;

    @Override
    protected String onGetToolbarTitleResource() {
        return "SurfaceView";
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_developer_surfaceview;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceView.post(new Runnable() {
            public void run() {
                mRealWidth = mSurfaceView.getWidth();
                mRealHeight = mSurfaceView.getHeight();
                k = mRealHeight / mRealWidth;
                multiple = mRealWidth / k;
                reStartAnimation();
            }
        });

        ballPaint = new Paint();
        ballPaint.setColor(Color.RED);
        ballPaint.setStrokeWidth(1);
        ballPaint.setAntiAlias(true);
        ballPaint.setDither(true);
        linePaint = new Paint();
        linePaint.setColor(Color.GRAY);
        linePaint.setStrokeWidth(1);
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        clearPaint = new Paint();
        clearPaint.setColor(Color.WHITE);
        //clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCanDraw = true;
        new RenderThread().start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCanDraw = false;
    }

    private class RenderThread extends Thread {
        @Override
        public void run() {
            while (mCanDraw) {
                if (mIsChanged) {
                    Canvas canvas = mSurfaceHolder.lockCanvas();
                    try {
                        draw(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                    mIsChanged = false;
                }
            }
        }
    }

    /**
     * canvas会保留上一次的内容
     */
    private void draw(Canvas canvas) {
        canvas.drawPaint(clearPaint);
        //canvas.drawLine(0, 0, mSurfaceView.getWidth(), mSurfaceView.getHeight(), linePaint);
        for (int i = 0; i + 1 < mPointFs.size(); i ++) {
            canvas.drawLine(mPointFs.get(i).x, mPointFs.get(i).y, mPointFs.get(i + 1).x, mPointFs.get(i + 1).y, ballPaint);
        }
        canvas.drawCircle(mPointF.x, mPointF.y, BALL_RADIUS, ballPaint);
    }

    private TypeEvaluator evaluator = new TypeEvaluator<PointF>() {

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            float xLarge = startValue.x + (fraction * (endValue.x - startValue.x));
            double xSmall = xLarge / multiple;
            double ySmall = Math.pow(xSmall, 2);
            float yLarge = (float) (ySmall * multiple);
            return new PointF(xLarge, yLarge);
        }
    };

    private ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mPointF = (PointF) animation.getAnimatedValue();
            mPointFs.add(mPointF);
            mIsChanged = true;
        }
    };

    @OnClick(R.id.fragment_developer_surfaceview_repeat)
    void reStartAnimation() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        mPointFs.clear();
        mAnimator = ValueAnimator.ofObject(evaluator, new PointF(0, 0), new PointF(mRealWidth, mRealHeight));
        mAnimator.addUpdateListener(updateListener);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(3000);
        mAnimator.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }
}
