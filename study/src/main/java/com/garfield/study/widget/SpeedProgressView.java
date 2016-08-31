package com.garfield.study.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
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

    public static final boolean StartTest = true;

    private float MaxSpeed = 30;
    private int Angle = 270;     //展开的总角度
    private int SpeedTextSize = dp2px(50);
    private int GearTextSize = dp2px(25);
    private int UnitTextSize = dp2px(15);
    private int DottedWidth = dp2px(4);
    private int TrackMargin = dp2px(25);
    private int TrackWidth = dp2px(25);
    private int HeadInWidth = TrackWidth;
    private int HeadInHeight = dp2px(5);
    private int HeadOutWidth = HeadInWidth + dp2px(12);
    private int HeadOutHeight = HeadInHeight + dp2px(12);

    private Paint mDottedArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mDottedArcPath = new Path();

    private Paint mTrackArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mTrackArcPath = new Path();
    private Paint mTrackMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mTrackMaskPath = new Path();

    private Paint mSpeedArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mSpeedArcPath = new Path();

    private Paint mBottomMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect mBottomMaskRect;

    private Paint mSpeedTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mSpeedTextBaseline;
    private Paint mGearTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mGearTextBaseline;
    private Paint mUnitTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mUnitTextBaseline;

    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mHeadInPaint;
    private Rect mHeadInRect;
    private Drawable mGreenHeadDrawable;
    private Drawable mRedHeadDrawable;
    private Drawable mHeadOutDrawable;
    private Rect mHeadOutRect;

    private RectF mTrackArcRect;

    private int mWidth;
    private int mHeight;   //根据角度计算的实际的高度，非控件高度

    //speed传入前
    private float mCurrentSpeed;
    //speed传入后
    private float mToAngle = 270 - Angle / 2;    //0度

    private int mGearSpeed;

    Handler mHandler = new Handler();

    public SpeedProgressView(Context context) {
        this(context, null);
    }

    public SpeedProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        startTest();
    }

    public void setSpeed(float speed) {
        if (speed > MaxSpeed) {
            speed = MaxSpeed;
        }
        animateTo(speed);
        mCurrentSpeed = speed;
    }

    public void setGearSpeed(int speed) {
        mGearSpeed = speed;
    }

    public void setMaxSpeed(float speed) {
        MaxSpeed = speed;
    }

    private void initView() {
        if (mTrackArcRect != null) {
            return;
        }
        setBackgroundColor(Color.BLACK);

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
        matrix.setRotate(270-Angle/2, dottedArcRect.centerX(), dottedArcRect.centerY());   //旋转到起点
        dottedShader.setLocalMatrix(matrix);
        mDottedArcPaint.setShader(dottedShader);
        Path pointRect = new Path();
        pointRect.addRoundRect(new RectF(0, 0, DottedWidth*2, DottedWidth), 5, 5, Path.Direction.CW);
        mDottedArcPaint.setPathEffect(new PathDashPathEffect(pointRect, DottedWidth*5, 0, PathDashPathEffect.Style.ROTATE));
        mDottedArcPath.reset();
        mDottedArcPath.arcTo(dottedArcRect, 270 - Angle / 2, Angle);

        //轨道
        mTrackArcRect = new RectF(TrackMargin, TrackMargin, mWidth - TrackMargin, mWidth - TrackMargin);  //正方形
        mTrackArcPaint.setStyle(Paint.Style.STROKE);
        mTrackArcPaint.setStrokeWidth(TrackWidth);
        mTrackArcPaint.setShader(dottedShader);
        mTrackArcPath.reset();
        mTrackArcPath.arcTo(mTrackArcRect, 270 - Angle / 2, Angle);

        //轨道遮罩
        RectF trackMaskRect = new RectF(-mTrackArcRect.width()/2, -mTrackArcRect.height()/2,
                mTrackArcRect.width()/2, mTrackArcRect.height()/2);  //较为特殊，要被旋转
        Shader trackMaskShader = new SweepGradient(0, 0, new int[]{     //这个地方必须是0,0，因为旋转！！！
                Color.parseColor("#BB000000"),
                Color.parseColor("#FF000000"),
                Color.parseColor("#FF000000")},
                new float[]{0, 0.3f, 1});
        Matrix trackMaskMatrix = new Matrix();
        trackMaskMatrix.setRotate(270-Angle/2, 0, 0);   //必须是0,0
        trackMaskShader.setLocalMatrix(trackMaskMatrix);
        mTrackMaskPaint.setStyle(Paint.Style.STROKE);
        mTrackMaskPaint.setStrokeWidth(TrackWidth+5);
        mTrackMaskPaint.setShader(trackMaskShader);
        mTrackMaskPath.reset();
        mTrackMaskPath.arcTo(trackMaskRect, 270 - Angle / 2, Angle);

        //速度
        mSpeedArcPaint.setStyle(Paint.Style.STROKE);
        mSpeedArcPaint.setStrokeWidth(TrackWidth);
        mSpeedArcPaint.setShader(dottedShader);
        mSpeedArcPath.reset();
        mSpeedArcPath.arcTo(mTrackArcRect, 270 - Angle / 2, 3);

        //底部遮罩
        mBottomMaskRect = new Rect(0, mWidth/2, mWidth, mHeight);
        Shader bottomMaskShader = new LinearGradient(0, dottedArcRect.centerY(), 0, mHeight,
                Color.parseColor("#00000000"),
                Color.parseColor("#FF000000"), Shader.TileMode.CLAMP);
        mBottomMaskPaint.setShader(bottomMaskShader);

        //速度头
        mGreenPaint.setColor(Color.parseColor("#00f79d"));
        mHeadInPaint = mGreenPaint;
        mRedPaint.setColor(Color.parseColor("#d00202"));
        mHeadInRect = new Rect(-(int)mTrackArcRect.width()/2- HeadInWidth /2,       //这两个较特殊，要被旋转
                -HeadInHeight /2, -(int)mTrackArcRect.width()/2+ HeadInWidth /2, HeadInHeight /2);
        mHeadOutRect = new Rect(-(int)mTrackArcRect.width()/2- HeadOutWidth /2,
                -HeadOutHeight /2, -(int)mTrackArcRect.width()/2+ HeadOutWidth /2, HeadOutHeight /2);
        mGreenHeadDrawable = getResources().getDrawable(R.drawable.speed_progress_head_green);
        mGreenHeadDrawable.setBounds(mHeadOutRect);
        mRedHeadDrawable = getResources().getDrawable(R.drawable.speed_progress_head_red);
        mRedHeadDrawable.setBounds(mHeadOutRect);
        mHeadOutDrawable = mGreenHeadDrawable;

        //字体
        Typeface typeFace = Typeface.createFromAsset(getResources().getAssets(), "NettoOT-Light.otf");
        mSpeedTextPaint.setTextSize(SpeedTextSize);
        mSpeedTextPaint.setColor(Color.WHITE);
        mSpeedTextPaint.setTypeface(typeFace);
        mSpeedTextPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = mSpeedTextPaint.getFontMetricsInt();
        mSpeedTextBaseline = (int) mTrackArcRect.centerY() - (fontMetrics.bottom + fontMetrics.top) / 2;
        mGearTextPaint.setTextSize(GearTextSize);
        mGearTextPaint.setColor(Color.WHITE);
        mGearTextPaint.setTypeface(typeFace);
        mGearTextPaint.setTextAlign(Paint.Align.CENTER);
        fontMetrics = mGearTextPaint.getFontMetricsInt();
        mGearTextBaseline = mWidth*9/32 - (fontMetrics.bottom + fontMetrics.top) / 2;
        mUnitTextPaint.setTextSize(UnitTextSize);
        mUnitTextPaint.setColor(Color.WHITE);
        mUnitTextPaint.setTypeface(typeFace);
        mUnitTextPaint.setTextAlign(Paint.Align.CENTER);
        fontMetrics = mUnitTextPaint.getFontMetricsInt();
        mUnitTextBaseline = mWidth*11/16 - (fontMetrics.bottom + fontMetrics.top) / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        mHeight = (int)(Math.sin(Math.PI*(Angle/2-90)/180) * (mWidth/2)) + mWidth/2;
        if (viewHeight < mHeight) {
            setMeasuredDimension(mWidth, mHeight);
        } else {
            setMeasuredDimension(mWidth, viewHeight);
        }
        initView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mTrackArcPath, mTrackArcPaint);

        canvas.save();
        canvas.translate(mTrackArcRect.centerX(), mTrackArcRect.centerY());
        canvas.rotate(mToAngle-(270-Angle/2));
        canvas.drawPath(mTrackMaskPath, mTrackMaskPaint);
        canvas.restore();

        canvas.drawPath(mDottedArcPath, mDottedArcPaint);
        canvas.drawPath(mSpeedArcPath, mSpeedArcPaint);

        //mBottomMaskDrawable.draw(canvas);
        canvas.drawRect(mBottomMaskRect, mBottomMaskPaint);

        canvas.save();
        canvas.translate(mTrackArcRect.centerX(), mTrackArcRect.centerY());
        canvas.rotate(mToAngle-180);
        canvas.drawRect(mHeadInRect, mHeadInPaint);
        mHeadOutDrawable.draw(canvas);
        canvas.restore();

        canvas.drawText(mCurrentSpeed+"", mTrackArcRect.centerX(), mSpeedTextBaseline, mSpeedTextPaint);
        canvas.drawText(mGearSpeed+"档", mTrackArcRect.centerX(), mGearTextBaseline, mGearTextPaint);
        canvas.drawText("Km/h", mTrackArcRect.centerX(), mUnitTextBaseline, mUnitTextPaint);

        //边框
        //canvas.drawRect(new RectF(0, 0, mWidth, mHeight), mDottedArcPaint);
        //canvas.drawRect(mTrackArcRect, mDottedArcPaint);
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

    private void setCurrentSpeed(float currentSpeed) {
        mSpeedArcPath.reset();
        float sweepAngle = currentSpeed / MaxSpeed * Angle;
        mToAngle = 270 - Angle / 2 + sweepAngle;
        if (mToAngle < 350) {
            mHeadInPaint = mGreenPaint;
            mHeadOutDrawable = mGreenHeadDrawable;
        } else {
            mHeadInPaint = mRedPaint;
            mHeadOutDrawable = mRedHeadDrawable;
        }
        mSpeedArcPath.arcTo(mTrackArcRect, 270 - Angle / 2, sweepAngle);
        postInvalidate();
    }

    private void startTest() {
        if (StartTest) {
            mHandler.postDelayed(mRunnable, 2000);
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            final DecimalFormat df = new DecimalFormat("#00.0");
            String value = df.format(Math.random() * MaxSpeed);
            setSpeed(Float.parseFloat(value));
            mHandler.postDelayed(mRunnable, 2000);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }

    private int dp2px(float var1) {
        float var2 = getResources().getDisplayMetrics().density;
        return (int)(var1 * var2 + 0.5F);
    }
}
