package com.garfield.baselib.ui.crop.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.garfield.baselib.ui.crop.callback.ModuleProxy;
import com.garfield.baselib.ui.crop.utils.RectUtils;
import com.garfield.baselib.utils.system.ScreenUtil;

/**
 * Created by gaowei3 on 2017/1/20.
 */

public class OverlayView extends View {

    protected int mThisWidth, mThisHeight;
    /**
     * 裁剪框的区域
     * mCropRect是以完整的OverlayView，忽略padding的区域为父元素
     */
    private final RectF mCropViewRect = new RectF();   //包含了padding
    private final RectF mTempRect = new RectF();

    private ModuleProxy mModuleProxy;

    private boolean mDrawGrid = true;
    private boolean mCanChangeSize = true;
    private boolean mCircleCrop = false;

    private boolean mHoldEnabled;
    private float mCropRatio;

    // 绘图
    private int mCropGridRowCount = 2, mCropGridColumnCount = 2;
    private float[] mGridPoints = new float[(mCropGridRowCount) * 4 + (mCropGridColumnCount) * 4];   //内部的点
    private Paint mCropGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);    //内部横竖线
    private Paint mCropBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);   //外边框
    protected float[] mCropBorderCorners;    //4个角
    private Paint mCropBorderCornersPaint = new Paint(Paint.ANTI_ALIAS_FLAG);    //外边框4个角
    private int mCropBorderCornerLength = ScreenUtil.dp2px(10);   //4个角的长度
    private Path mCircularPath = new Path();

    // 拖动
    private int mCurrentTouchCornerIndex = -1;
    private float mPreviousTouchX = -1, mPreviousTouchY = -1;
    private float mDownTouchX = -1, mDownTouchY = -1;
    private int mTouchPointThreshold = ScreenUtil.dp2px(30);
    private int mCropRectMinSize = ScreenUtil.dp2px(100);

    public OverlayView(Context context) {
        this(context, null);
    }

    public OverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // 关闭硬件加速
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        mCropGridPaint.setColor(Color.parseColor("#80FFFFFF"));
        mCropGridPaint.setStrokeWidth(ScreenUtil.dp2px(1));
        mCropBorderPaint.setColor(Color.WHITE);
        mCropBorderPaint.setStrokeWidth(ScreenUtil.dp2px(1));
        mCropBorderPaint.setStyle(Paint.Style.STROKE);
        mCropBorderCornersPaint.setStrokeWidth(ScreenUtil.dp2px(3));
        mCropBorderCornersPaint.setColor(Color.WHITE);
        mCropBorderCornersPaint.setStyle(Paint.Style.STROKE);
        setVisibility(INVISIBLE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mThisWidth = getWidth() - getPaddingRight() - getPaddingLeft();
            mThisHeight = getHeight() - getPaddingBottom() - getPaddingTop();
        }
    }

    void setModuleProxy(ModuleProxy moduleProxy) {
        mModuleProxy = moduleProxy;
    }

    /**
     * 从Image传过来，imgRect需要加上padding
     */
    void setTargetCropRatio(float ratio, RectF imgRect) {
        // 执行后表示允许裁剪，所以显示
        setVisibility(VISIBLE);
        setCropBounds(ratio, imgRect);
        invalidate();
    }

    /**
     * 设置裁剪边界
     */
    void setCropBounds(float ratio, RectF imgRect) {
        mCropRatio = ratio;

        float imgWidth = imgRect.width();
        float imgHeight = imgRect.height();
        // 以宽为标准
        int height = (int) (imgWidth / ratio);
        if (height > imgHeight) {
            // 比如正方形比例，图像是矮胖，这时需要重新以高为标准
            int width = (int) (imgHeight * ratio);
            float halfDiff = (getWidth() - width) / 2;
            mCropViewRect.set(halfDiff, getPaddingTop() + imgRect.top,
                    width + halfDiff, getPaddingTop() + imgRect.bottom);
        } else {
            // 比如正方形比例，图像是瘦高，不需要重新计算了
            float halfDiff = (getHeight() - height) / 2;
            mCropViewRect.set(getPaddingLeft() + imgRect.left, halfDiff,
                    getPaddingLeft() + imgRect.right, height + halfDiff);
        }

        if (mModuleProxy != null) {
            mModuleProxy.onCropRectUpdated(mCropViewRect);
        }
    }

    void setHoldEnabled(boolean holdEnabled) {
        mHoldEnabled = holdEnabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            mCurrentTouchCornerIndex = getCurrentTouchIndex(x, y);
            boolean shouldHandle = mCurrentTouchCornerIndex != -1;
            if (!shouldHandle) {
                mPreviousTouchX = -1;
                mPreviousTouchY = -1;
            } else {
                mPreviousTouchX = x;
                mPreviousTouchY = y;
            }
            return shouldHandle;
        }

        if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
            if (event.getPointerCount() == 1 && mCurrentTouchCornerIndex != -1) {

//                x = Math.min(Math.max(x, getPaddingLeft()), getWidth() - getPaddingRight());
//                y = Math.min(Math.max(y, getPaddingTop()), getHeight() - getPaddingBottom());

                if (mHoldEnabled && mCurrentTouchCornerIndex != 4) {
                    float fixedAngleX = -1, fixedAngleY = -1;
                    if (mCurrentTouchCornerIndex == 0) {
                        fixedAngleX = mCropViewRect.right;
                        fixedAngleY = mCropViewRect.bottom;
                    } else if (mCurrentTouchCornerIndex == 1) {
                        fixedAngleX = mCropViewRect.left;
                        fixedAngleY = mCropViewRect.bottom;
                    } else if (mCurrentTouchCornerIndex == 2) {
                        fixedAngleX = mCropViewRect.left;
                        fixedAngleY = mCropViewRect.top;
                    } else if (mCurrentTouchCornerIndex == 3) {
                        fixedAngleX = mCropViewRect.right;
                        fixedAngleY = mCropViewRect.top;
                    }
                    double angleDistance = Math.sqrt((fixedAngleX - x) * (fixedAngleX - x) + (fixedAngleY - y) * (fixedAngleY - y));
                    float lengthOfSideX = (float) (angleDistance / Math.sqrt(mCropRatio * mCropRatio + 1) * mCropRatio);
                    float lengthOfSideY = (float) (angleDistance / Math.sqrt(mCropRatio * mCropRatio + 1));
                    if (mCurrentTouchCornerIndex == 0) {
                        x = fixedAngleX - lengthOfSideX;
                        y = fixedAngleY - lengthOfSideY;
                    } else if (mCurrentTouchCornerIndex == 1) {
                        x = fixedAngleX + lengthOfSideX;
                        y = fixedAngleY - lengthOfSideY;
                    } else if (mCurrentTouchCornerIndex == 2) {
                        x = fixedAngleX + lengthOfSideX;
                        y = fixedAngleY + lengthOfSideY;
                    } else if (mCurrentTouchCornerIndex == 3) {
                        x = fixedAngleX - lengthOfSideX;
                        y = fixedAngleY + lengthOfSideY;
                    }
                }

                if (new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom()).contains(x, y)) {
                    changeCropBounds(x, y);
                    mPreviousTouchX = x;
                    mPreviousTouchY = y;
                }
                return true;
            }
        }

        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            mPreviousTouchX = -1;
            mPreviousTouchY = -1;
            mCurrentTouchCornerIndex = -1;
            if (mModuleProxy != null) {
                mModuleProxy.onCropRectUpdated(mCropViewRect);
            }
        }
        return true;
    }

    /**
     * * The order of the corners is:
     * 0------->1
     * ^        |
     * |   4    |
     * |        v
     * 3<-------2
     */
    private void changeCropBounds(float touchX, float touchY) {
        mTempRect.set(mCropViewRect);
        switch (mCurrentTouchCornerIndex) {
            case 0:
                mTempRect.set(touchX, touchY, mCropViewRect.right, mCropViewRect.bottom);
                break;
            case 1:
                mTempRect.set(mCropViewRect.left, touchY, touchX, mCropViewRect.bottom);
                break;
            case 2:
                mTempRect.set(mCropViewRect.left, mCropViewRect.top, touchX, touchY);
                break;
            case 3:
                mTempRect.set(touchX, mCropViewRect.top, mCropViewRect.right, touchY);
                break;
            case 4:
                float dx = touchX - mPreviousTouchX;
                float dy = touchY - mPreviousTouchY;
                if (mTempRect.left + dx < getPaddingLeft() || mTempRect.right + dx > getWidth() - getPaddingRight()) {
                    dx = 0;
                }
                if (mTempRect.top + dy < getPaddingTop() || mTempRect.bottom + dy > getHeight() - getPaddingBottom()) {
                    dy = 0;
                }
                mTempRect.offset(dx, dy);     //位移
                if (dx != 0 || dy != 0) {
                    mCropViewRect.set(mTempRect);
                    invalidate();
                }
                return;
        }
        // 用来控制最小尺寸
        boolean changeHeight = mTempRect.height() >= mCropRectMinSize / mCropRatio;
        boolean changeWidth = mTempRect.width() >= mCropRectMinSize;
        mCropViewRect.set(
                changeWidth ? mTempRect.left : mCropViewRect.left,
                changeHeight ? mTempRect.top : mCropViewRect.top,
                changeWidth ? mTempRect.right : mCropViewRect.right,
                changeHeight ? mTempRect.bottom : mCropViewRect.bottom);

        if (changeHeight || changeWidth) {
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        updatePoints();
        drawDimmedLayer(canvas);
        drawCropGrid(canvas);
    }

    private void drawDimmedLayer(Canvas canvas) {
        canvas.save();
        if (mCircleCrop) {
            canvas.clipPath(mCircularPath, Region.Op.DIFFERENCE);
        } else {
            canvas.clipRect(mCropViewRect, Region.Op.DIFFERENCE);
        }
        canvas.drawColor(Color.parseColor("#8c000000"));
        canvas.restore();
    }

    private void drawCropGrid(@NonNull Canvas canvas) {
        // 画外边框
        canvas.drawRect(mCropViewRect, mCropBorderPaint);

        // 画圆环
        if (mCircleCrop) {
            canvas.drawCircle(mCropViewRect.centerX(), mCropViewRect.centerY(), Math.min(mCropViewRect.width(), mCropViewRect.height()) / 2.f, mCropBorderPaint);
        }

        // 画横竖线
        if (mDrawGrid) {
            canvas.drawLines(mGridPoints, mCropGridPaint);
        }

        // 画4个角
        if (mCanChangeSize) {
            canvas.save();
            mTempRect.set(mCropViewRect);
            // 把四个边除了四个角以外的部分，挖掉
            mTempRect.inset(mCropBorderCornerLength, -mCropBorderCornerLength);    //内缩
            canvas.clipRect(mTempRect, Region.Op.DIFFERENCE);
            mTempRect.set(mCropViewRect);
            mTempRect.inset(-mCropBorderCornerLength, mCropBorderCornerLength);
            canvas.clipRect(mTempRect, Region.Op.DIFFERENCE);
            canvas.drawRect(mCropViewRect, mCropBorderCornersPaint);
            canvas.restore();
        }
    }

    private void updatePoints() {
        if (mCircleCrop) {
            mCircularPath.reset();
            mCircularPath.addCircle(mCropViewRect.centerX(), mCropViewRect.centerY(),
                    Math.min(mCropViewRect.width(), mCropViewRect.height()) / 2.f, Path.Direction.CW);
        }

        if (mDrawGrid) {
            int index = 0;
            for (int i = 0; i < mCropGridRowCount; i++) {
                mGridPoints[index++] = mCropViewRect.left;
                mGridPoints[index++] = (mCropViewRect.height() * (((float) i + 1.0f) / (float) (mCropGridRowCount + 1))) + mCropViewRect.top;
                mGridPoints[index++] = mCropViewRect.right;
                mGridPoints[index++] = (mCropViewRect.height() * (((float) i + 1.0f) / (float) (mCropGridRowCount + 1))) + mCropViewRect.top;
            }
            for (int i = 0; i < mCropGridColumnCount; i++) {
                mGridPoints[index++] = (mCropViewRect.width() * (((float) i + 1.0f) / (float) (mCropGridColumnCount + 1))) + mCropViewRect.left;
                mGridPoints[index++] = mCropViewRect.top;
                mGridPoints[index++] = (mCropViewRect.width() * (((float) i + 1.0f) / (float) (mCropGridColumnCount + 1))) + mCropViewRect.left;
                mGridPoints[index++] = mCropViewRect.bottom;
            }
        }
    }

    /**
     * * The order of the corners in the float array is:
     * 0------->1
     * ^        |
     * |   4    |
     * |        v
     * 3<-------2
     *
     * @return - index of corner that is being dragged
     */
    private int getCurrentTouchIndex(float touchX, float touchY) {
        int closestPointIndex = -1;
        double closestPointDistance = mTouchPointThreshold;
        mCropBorderCorners = RectUtils.getCornersFromRect(mCropViewRect);
        for (int i = 0; i < 8; i += 2) {
            double distanceToCorner = Math.sqrt(Math.pow(touchX - mCropBorderCorners[i], 2)
                    + Math.pow(touchY - mCropBorderCorners[i + 1], 2));
            // 不仅在阈值以内，还要求是距离最近的那个点
            if (distanceToCorner < closestPointDistance) {
                closestPointDistance = distanceToCorner;
                closestPointIndex = i / 2;
            }
        }

        if (closestPointIndex < 0 && mCropViewRect.contains(touchX, touchY)) {
            return 4;
        }
        return closestPointIndex;
    }
}
