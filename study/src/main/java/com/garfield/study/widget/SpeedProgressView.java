package com.garfield.study.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.garfield.baselib.utils.L;

import java.text.DecimalFormat;

/**
 * Created by gaowei3 on 2016/8/29.
 */
public class SpeedProgressView extends View {

    public static final int Angle = 358;
    public static final int DottedWidth = 6;   //最好是偶数
    public static final int TrackMargin = 40;
    public static final int TrackWidth = 30;
    public static final int MaxSpeed = 100;

    private Paint mDottedArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mDottedArcPath = new Path();

    private Paint mTrackArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mTrackArcPath = new Path();

    private Paint mSpeedArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mSpeedArcPath = new Path();

    private Paint mSpeedTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mSpeedTextBaseline;

    private int mWidth;
    private int mHeight;

    private float mCurrentSpeed;
    private RectF trackArcRect;

    Handler mHandler = new Handler();

    public SpeedProgressView(Context context) {
        this(context, null);
    }

    public SpeedProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final DecimalFormat df = new DecimalFormat("#00.00");
                while (true) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            String value = df.format(Math.random() * MaxSpeed);
                            setSpeed(Float.parseFloat(value));

                        }
                    });
                }
            }
        }).start();
    }

    public void setSpeed(float speed) {
        if (speed > MaxSpeed) {
            speed = MaxSpeed;
        }
        animateTo(speed);
        mCurrentSpeed = speed;
    }

    private void initView() {
        //大圈
        RectF dottedArcRect = new RectF(DottedWidth/2, DottedWidth/2, mWidth - DottedWidth/2, mWidth - DottedWidth/2);
        mDottedArcPaint.setStyle(Paint.Style.STROKE);
        mDottedArcPaint.setStrokeWidth(DottedWidth);
        mDottedArcPaint.setShader(new SweepGradient(dottedArcRect.centerX(), dottedArcRect.centerY(), new int[]{
                Color.parseColor("#00f4a6"),
                Color.parseColor("#00eac8"),
                Color.parseColor("#0f84df"),
                Color.parseColor("#00e9cc"),
                Color.parseColor("#00ff82"),
                Color.parseColor("#00f4a6")},
                new float[]{0, 30 / 360f, 150 / 360f, 220 / 360f, 270 / 360f, 1}));
        mDottedArcPath.reset();
        mDottedArcPath.arcTo(dottedArcRect, 270 - Angle / 2, Angle);
        //点
        Path pointRect = new Path();
        pointRect.addRoundRect(new RectF(0, 0, DottedWidth*2, DottedWidth), 5, 5, Path.Direction.CW);
        mDottedArcPaint.setPathEffect(new PathDashPathEffect(pointRect, DottedWidth*5, 0, PathDashPathEffect.Style.ROTATE));

        trackArcRect = new RectF(TrackMargin, TrackMargin, mWidth - TrackMargin, mWidth - TrackMargin);
        mTrackArcPaint.setStyle(Paint.Style.STROKE);
        mTrackArcPaint.setStrokeWidth(TrackWidth);
        mTrackArcPaint.setColor(Color.BLACK);
        mTrackArcPath.reset();
        mTrackArcPath.arcTo(trackArcRect, 270 - Angle / 2, Angle);

        mSpeedArcPaint.setStyle(Paint.Style.STROKE);
        mSpeedArcPaint.setStrokeWidth(TrackWidth);
        mSpeedArcPaint.setColor(Color.GREEN);
        mSpeedArcPath.reset();
        mSpeedArcPath.arcTo(trackArcRect, 270 - Angle / 2, 3);

        mSpeedTextPaint.setTextSize(72);
        mSpeedTextPaint.setColor(Color.WHITE);
        mSpeedTextPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = mSpeedTextPaint.getFontMetricsInt();
        mSpeedTextBaseline = (int)trackArcRect.centerY() - (fontMetrics.bottom + fontMetrics.top) / 2;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = mWidth;//(int)(Math.sin(Angle /2 - 90) * (mWidth/2)) + mWidth/2;
        setMeasuredDimension(mWidth, mHeight);
        initView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mTrackArcPath, mTrackArcPaint);
        canvas.drawPath(mDottedArcPath, mDottedArcPaint);
        canvas.drawPath(mSpeedArcPath, mSpeedArcPaint);
        canvas.drawText(mCurrentSpeed+"", trackArcRect.centerX(), mSpeedTextBaseline, mSpeedTextPaint);
    }

    private void animateTo(float toSpeed) {
        Animator animator = ObjectAnimator.ofFloat(this, "currentSpeed", mCurrentSpeed, toSpeed);
        animator.setInterpolator(new LinearInterpolator());
        float percent = Math.abs(toSpeed - mCurrentSpeed) / 40;
        if (0 <= percent && percent < 0.3f) {
            animator.setDuration((long) (1200 * percent));
        } else if (0.3f <= percent && percent < 0.6f) {
            animator.setDuration((long) (1000 * percent));
        } else {
            animator.setDuration((long) (800 * percent));
        }
        animator.start();
    }

    public void setCurrentSpeed(float currentSpeed) {
        mSpeedArcPath.reset();
        mSpeedArcPath.arcTo(trackArcRect, 270 - Angle / 2, currentSpeed / MaxSpeed * Angle);
        postInvalidate();
    }
}
