package com.garfield.study.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.garfield.study.R;

import java.text.DecimalFormat;

/**
 * Created by gaowei3 on 2016/8/29.
 */
public class SpeedProgressView extends View {

    private int Angle = 270;
    private int DottedWidth = 6;   //最好是偶数
    private int TrackMargin = 40;
    private int TrackWidth = 30;
    private int MaxSpeed = 40;

    private Paint mDottedArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mDottedArcPath = new Path();

    private Paint mTrackArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mTrackArcPath = new Path();

    private Paint mSpeedArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mSpeedArcPath = new Path();

    private Paint mSpeedTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mSpeedTextBaseline;

    private Paint mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect mMaskRect;
    private Drawable mMaskDrawable;

    private Paint mHeadPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mHeadLightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mWidth;
    private int mHeight;

    //speed传入前
    private float mCurrentSpeed;
    //speed传入后
    private float mToAngle = 270 - Angle / 2;
    private RectF mTrackArcRect;

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
        Shader dottedShader = new SweepGradient(dottedArcRect.centerX(), dottedArcRect.centerY(), new int[]{
                Color.parseColor("#0c90cc"),
                Color.parseColor("#0b9fc2"),
                Color.parseColor("#09abba"),
                Color.parseColor("#08bbb0"),
                Color.parseColor("#06cea3"),
                Color.parseColor("#04dd99"),
                Color.parseColor("#03ea90"),
                Color.parseColor("#797448"),
                Color.parseColor("#ea0303"),
                Color.parseColor("#ea0303")},
                new float[]{0, 30/360f, 60/360f, 90/360f, 120/360f, 150/360f, 180/360f, 210/360f, 240/360f, 1});
        Matrix matrix = new Matrix();
        matrix.setRotate(270-Angle/2, dottedArcRect.centerX(), dottedArcRect.centerY());
        dottedShader.setLocalMatrix(matrix);
        mDottedArcPaint.setShader(dottedShader);
        Path pointRect = new Path();
        pointRect.addRoundRect(new RectF(0, 0, DottedWidth*2, DottedWidth), 5, 5, Path.Direction.CW);
        mDottedArcPaint.setPathEffect(new PathDashPathEffect(pointRect, DottedWidth*5, 0, PathDashPathEffect.Style.ROTATE));
        mDottedArcPath.reset();
        mDottedArcPath.arcTo(dottedArcRect, 270 - Angle / 2, Angle);

        //轨道
        mTrackArcRect = new RectF(TrackMargin, TrackMargin, mWidth - TrackMargin, mWidth - TrackMargin);
        mTrackArcPaint.setStyle(Paint.Style.STROKE);
        mTrackArcPaint.setStrokeWidth(TrackWidth);
        mTrackArcPaint.setColor(Color.BLACK);
        mTrackArcPath.reset();
        mTrackArcPath.arcTo(mTrackArcRect, 270 - Angle / 2, Angle);

        //速度
        mSpeedArcPaint.setStyle(Paint.Style.STROKE);
        mSpeedArcPaint.setStrokeWidth(TrackWidth);
        mSpeedArcPaint.setShader(dottedShader);
        mSpeedArcPath.reset();
        mSpeedArcPath.arcTo(mTrackArcRect, 270 - Angle / 2, 3);

        //遮罩
        mMaskRect = new Rect(0, mWidth/2, mWidth, mHeight);
        mMaskDrawable = getResources().getDrawable(R.drawable.progress_shadow);
        mMaskDrawable.setBounds(mMaskRect);

        //速度头
        mHeadPaint.setStyle(Paint.Style.STROKE);
        mHeadPaint.setStrokeWidth(TrackWidth);
        mHeadPaint.setColor(Color.parseColor("#00f79d"));
        mHeadLightPaint.setStyle(Paint.Style.STROKE);
        mHeadLightPaint.setStrokeWidth(TrackWidth+8);
        mHeadLightPaint.setColor(Color.parseColor("#8800f79d"));

        //字体
        mSpeedTextPaint.setTextSize(72);
        mSpeedTextPaint.setColor(Color.WHITE);
        mSpeedTextPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = mSpeedTextPaint.getFontMetricsInt();
        mSpeedTextBaseline = (int) mTrackArcRect.centerY() - (fontMetrics.bottom + fontMetrics.top) / 2;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = (int)(Math.sin(Math.PI*(Angle/2-90)/180) * (mWidth/2)) + mWidth/2;
        setMeasuredDimension(mWidth, mHeight);
        initView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mTrackArcPath, mTrackArcPaint);
        canvas.drawPath(mDottedArcPath, mDottedArcPaint);
        canvas.drawPath(mSpeedArcPath, mSpeedArcPaint);
        mMaskDrawable.draw(canvas);
        canvas.drawArc(mTrackArcRect, mToAngle - 3.5f, 4, false, mHeadLightPaint);
        canvas.drawArc(mTrackArcRect, mToAngle - 3, 3, false, mHeadPaint);

        canvas.drawText(mCurrentSpeed+"", mTrackArcRect.centerX(), mSpeedTextBaseline, mSpeedTextPaint);

        //边框
        //canvas.drawRect(new RectF(0, 0, mWidth, mHeight), mDottedArcPaint);
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
        float sweepAngle = currentSpeed / MaxSpeed * Angle;
        mToAngle = 270 - Angle / 2 + sweepAngle;
        if (mToAngle < 340) {
            mHeadPaint.setColor(Color.parseColor("#00f79d"));
            mHeadLightPaint.setColor(Color.parseColor("#5577F7C9"));
        } else {
            mHeadLightPaint.setColor(Color.parseColor("#66d00202"));
            mHeadPaint.setColor(Color.parseColor("#d00202"));
        }
        mSpeedArcPath.arcTo(mTrackArcRect, 270 - Angle / 2, sweepAngle);
        postInvalidate();
    }
}
