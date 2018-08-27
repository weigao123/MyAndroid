package com.garfield.study.backup.speed;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.animation.AccelerateDecelerateInterpolator;

import com.garfield.study.R;

import java.text.DecimalFormat;

/**
 * Created by gaowei3 on 2016/8/29.
 */
public class SpeedProgressView2 extends View {

    public static final boolean StartTest = true;
    private String BackgroundColor = "000000";

    private float MaxSpeed = 40;
    private float OverSpeed = 30;
    private int AnimatorTimePerAngle = 5;
    private int Angle = 270;     //展开的总角度
    private int UnitTextSize = dp2px(18);
    private int SpeedTextSize = dp2px(55);
    private int GearTextSize = dp2px(20);
    private int DottedWidth = dp2px(3);
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

    private Paint mBottomMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect mBottomMaskRect;

    private Paint mSpeedTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mSpeedTextBaseline;
    private Paint mGearTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mGearTextBaseline;
    private Bitmap mGearBgBitmap;
    private RectF mGearBgRect;
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
    private RectF mTrackMaskArcRect;

    private int mWidth;
    private int mHeight;   //根据角度计算的实际的高度，非控件高度

    //speed传入前
    private float mCurrentSpeed;
    private float mCurrentAngle = 270 - Angle / 2;    //以3点方向为0度
    //speed传入后
    private float mToSpeed;
    private float mToAngle = 270 - Angle / 2;
    //以最右为0基准的角度
    private float mThisAngle = 270 - Angle / 2;

    private int mGearSpeed;

    private Handler mHandler;
    private Animator mAnimator;

    public SpeedProgressView2(Context context) {
        this(context, null);
    }

    public SpeedProgressView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedProgressView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        startTest();
    }

    public void setSpeed(float speed) {
        if ((mAnimator != null && mAnimator.isRunning()) || speed == mToSpeed) {
            return;
        }
        if (speed > MaxSpeed) {
            speed = MaxSpeed;
        }
        mToSpeed = speed;
        float sweepAngle = mToSpeed / MaxSpeed * Angle;
        mToAngle = 270 - Angle / 2 + sweepAngle;
        animateTo();
        mCurrentSpeed = mToSpeed;
        mCurrentAngle = mToAngle;
    }

    public void setGearSpeed(int speed) {
        mGearSpeed = speed;
    }

    public void setMaxSpeed(float speed) {
        MaxSpeed = speed;
    }

    public void setOverSpeed(float speed) {
        OverSpeed = speed;
    }

    private void initView() {
        if (mTrackArcRect != null) return;
        setBackgroundColor(Color.parseColor("#" + BackgroundColor));

        //大圈
        RectF dottedArcRect = new RectF(DottedWidth/2, DottedWidth/2, mWidth - DottedWidth/2, mWidth - DottedWidth/2);
        mDottedArcPaint.setStyle(Paint.Style.STROKE);
        mDottedArcPaint.setStrokeWidth(DottedWidth);
        Shader dottedShader = new SweepGradient(dottedArcRect.centerX(), dottedArcRect.centerY(), new int[]{
                Color.parseColor("#0c90cc"),   //BB是因为要暗一点
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
        matrix.setRotate(270-Angle/2, dottedArcRect.centerX(), dottedArcRect.centerY());   //把颜色起点旋转到speed起点
        dottedShader.setLocalMatrix(matrix);
        mDottedArcPaint.setShader(dottedShader);
        Path pointRect = new Path();
        pointRect.addRoundRect(new RectF(0, 0, DottedWidth*2, DottedWidth), 5, 5, Path.Direction.CW);   //画一个圆角矩形
        mDottedArcPaint.setPathEffect(new PathDashPathEffect(pointRect, DottedWidth*6, 0, PathDashPathEffect.Style.ROTATE));  //可以把任何path作为虚线的片段
        mDottedArcPath.reset();
        mDottedArcPath.arcTo(dottedArcRect, 270 - Angle / 2, Angle);

        //轨道底座
        mTrackArcRect = new RectF(TrackMargin, TrackMargin, mWidth - TrackMargin, mWidth - TrackMargin);  //正方形，要比点点靠内
        mTrackArcPaint.setStyle(Paint.Style.STROKE);
        mTrackArcPaint.setStrokeWidth(TrackWidth);
        Shader trackShader = new SweepGradient(dottedArcRect.centerX(), dottedArcRect.centerY(), new int[]{
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
        trackShader.setLocalMatrix(matrix);     //颜色旋转
        mTrackArcPaint.setShader(trackShader);
        mTrackArcPath.reset();
        mTrackArcPath.arcTo(mTrackArcRect, 270 - Angle / 2, Angle);   //角度铺满

        //轨道灰度遮罩
        mTrackMaskArcRect = new RectF(-mTrackArcRect.width()/2, -mTrackArcRect.height()/2,
                mTrackArcRect.width()/2, mTrackArcRect.height()/2);  //移动画布后原点变成了中心，较为特殊，因为要被旋转
        Shader trackMaskShader = new SweepGradient(0, 0, new int[]{     //这个地方必须是0,0，因为旋转！！！
                Color.parseColor("#BB" + BackgroundColor),
                Color.parseColor("#FF" + BackgroundColor),
                Color.parseColor("#FF" + BackgroundColor)},
                new float[]{0, 0.3f, 1});
        Matrix trackMaskMatrix = new Matrix();
        trackMaskMatrix.setRotate(270-Angle/2, 0, 0);   //必须是0,0
        trackMaskShader.setLocalMatrix(trackMaskMatrix);
        mTrackMaskPaint.setStyle(Paint.Style.STROKE);
        mTrackMaskPaint.setStrokeWidth(TrackWidth+5);
        mTrackMaskPaint.setShader(trackMaskShader);
        mTrackMaskPath.reset();
        mTrackMaskPath.arcTo(mTrackMaskArcRect, 270 - Angle / 2, Angle);   //角度铺满

        //底部遮罩
        mBottomMaskRect = new Rect(0, mWidth/2, mWidth, mHeight);
        Shader bottomMaskShader = new LinearGradient(0, mTrackArcRect.centerY(), 0, mHeight,
                Color.parseColor("#00" + BackgroundColor),
                Color.parseColor("#FF" + BackgroundColor), Shader.TileMode.CLAMP);
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
        mUnitTextPaint.setTextSize(UnitTextSize);
        mUnitTextPaint.setColor(Color.WHITE);
        mUnitTextPaint.setTypeface(typeFace);
        mUnitTextPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = mUnitTextPaint.getFontMetricsInt();
        mUnitTextBaseline = mWidth*9/32 - (fontMetrics.bottom + fontMetrics.top) / 2;
        mSpeedTextPaint.setTextSize(SpeedTextSize);
        mSpeedTextPaint.setColor(Color.WHITE);
        mSpeedTextPaint.setTypeface(typeFace);
        mSpeedTextPaint.setTextAlign(Paint.Align.CENTER);
        fontMetrics = mSpeedTextPaint.getFontMetricsInt();
        mSpeedTextBaseline = (int) mTrackArcRect.centerY() - (fontMetrics.bottom + fontMetrics.top) / 2;
        mGearTextPaint.setTextSize(GearTextSize);
        mGearTextPaint.setColor(Color.WHITE);
        mGearTextPaint.setTypeface(typeFace);
        mGearTextPaint.setTextAlign(Paint.Align.CENTER);
        fontMetrics = mGearTextPaint.getFontMetricsInt();
        mGearTextBaseline = mWidth*11/16 - (fontMetrics.bottom + fontMetrics.top) / 2;
        mGearBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.speed_progress_gear_bg);
        mGearBgRect = new RectF(mWidth/2-GearTextSize*1.2f, mGearTextBaseline-GearTextSize - dp2px(2), mWidth/2+GearTextSize*1.2f, mGearTextBaseline + dp2px(5));
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

        canvas.drawPath(mDottedArcPath, mDottedArcPaint);
        canvas.drawPath(mTrackArcPath, mTrackArcPaint);

        // 绘制轨道遮罩
        // 只有旋转时，才会把渐变色的颜色起点也旋转
        canvas.save();
        canvas.translate(mTrackArcRect.centerX(), mTrackArcRect.centerY());
        canvas.rotate(mThisAngle - (270 - Angle / 2));
        canvas.drawPath(mTrackMaskPath, mTrackMaskPaint);
        canvas.restore();

        // 底部遮罩
        canvas.drawRect(mBottomMaskRect, mBottomMaskPaint);

        // 绘制光棒
        canvas.save();
        canvas.translate(mTrackArcRect.centerX(), mTrackArcRect.centerY());
        canvas.rotate(mThisAngle - 180);
        canvas.drawRect(mHeadInRect, mHeadInPaint);
        mHeadOutDrawable.draw(canvas);
        canvas.restore();

        // 文字
        canvas.drawText("Km/h", mTrackArcRect.centerX(), mUnitTextBaseline, mUnitTextPaint);
        canvas.drawText(mCurrentSpeed+"", mTrackArcRect.centerX(), mSpeedTextBaseline, mSpeedTextPaint);
        canvas.drawBitmap(mGearBgBitmap, null, mGearBgRect, null);
        canvas.drawText(mGearSpeed+"档", mTrackArcRect.centerX(), mGearTextBaseline, mGearTextPaint);

        //边框
        //canvas.drawRect(new RectF(0, 0, mWidth, mHeight), mDottedArcPaint);
        //canvas.drawRect(mTrackArcRect, mDottedArcPaint);
    }

    private void animateTo() {
        mAnimator = ObjectAnimator.ofFloat(this, "currentSpeed", mCurrentSpeed, mToSpeed);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        float diffAngle = Math.abs(mToAngle - mCurrentAngle);
        int diffTime = (int)(diffAngle * AnimatorTimePerAngle);
        if (diffTime < 500) {
            diffTime = 500;
        }
        mAnimator.setDuration(diffTime);
        mAnimator.start();
    }

    private void setCurrentSpeed(float thisSpeed) {
        mTrackMaskPath.reset();
        float sweepAngle = thisSpeed / MaxSpeed * Angle;
        float overSpeedSweepAngle = OverSpeed / MaxSpeed * Angle;   //超速时的角度
        mThisAngle = 270 - Angle / 2 + sweepAngle;
        if (sweepAngle < overSpeedSweepAngle) {
            mHeadInPaint = mGreenPaint;
            mHeadOutDrawable = mGreenHeadDrawable;
        } else {
            mHeadInPaint = mRedPaint;
            mHeadOutDrawable = mRedHeadDrawable;
        }
        // 这里只能控制绘制起点，无法控制颜色起点，
        // 只需要在(Angle / 2 + 270) - mThisAngle这个长度有内容
        mTrackMaskPath.arcTo(mTrackMaskArcRect, 270 - Angle / 2, (Angle / 2 + 270) - mThisAngle);    //起点必须是这个，因为要去旋转
        postInvalidate();
    }

    private void startTest() {
        if (StartTest) {
            final DecimalFormat df = new DecimalFormat("#00.0");
            final int delayTime = 2000;
            mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String value = df.format(Math.random() * MaxSpeed);
                    float speed = Float.parseFloat(value);
                    setSpeed(speed);
                    int gear = 0;
                    if (speed <= MaxSpeed/4 && speed > 0) {
                        gear = 1;
                    } else if (speed <= MaxSpeed * 2/4) {
                        gear = 2;
                    } else if (speed <= MaxSpeed * 3/4) {
                        gear = 3;
                    } else if (speed <= MaxSpeed * 4/4) {
                        gear = 4;
                    }
                    setGearSpeed(gear);
//                    if (mCurrentSpeed == MaxSpeed) {
//                        setSpeed(0);
//                    } else {
//                        setSpeed(MaxSpeed);
//                    }
                    mHandler.postDelayed(this, delayTime);
                }
            }, delayTime);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    private int dp2px(float var1) {
        float var2 = getResources().getDisplayMetrics().density;
        return (int)(var1 * var2 + 0.5F);
    }
}
