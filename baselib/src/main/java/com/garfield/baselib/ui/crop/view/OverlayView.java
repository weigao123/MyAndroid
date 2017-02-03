package com.garfield.baselib.ui.crop.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.garfield.baselib.R;
import com.garfield.baselib.ui.crop.callback.ModuleProxy;
import com.garfield.baselib.ui.crop.utils.RectUtils;
import com.garfield.baselib.utils.system.ScreenUtils;

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

    private float mTargetCropRatio;
    private ModuleProxy mModuleProxy;

    private boolean mDrawGrid = true;
    private boolean mCanChangeSize = true;
    private boolean mCircleCrop = false;

    private boolean mIsHold;

    // 绘图
    private int mCropGridRowCount = 2, mCropGridColumnCount = 2;
    private float[] mGridPoints = new float[(mCropGridRowCount) * 4 + (mCropGridColumnCount) * 4];   //内部的点
    private Paint mCropGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);    //内部横竖线
    private Paint mCropBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);   //外边框
    protected float[] mCropBorderCorners;    //4个角
    private Paint mCropBorderCornersPaint = new Paint(Paint.ANTI_ALIAS_FLAG);    //外边框4个角
    private int mCropBorderCornerLength = ScreenUtils.dp2px(10);   //4个角的长度
    private Path mCircularPath = new Path();

    // 拖动
    private int mCurrentTouchCornerIndex = -1;
    private float mPreviousTouchX = -1, mPreviousTouchY = -1;
    private int mTouchPointThreshold = ScreenUtils.dp2px(30);
    private int mCropRectMinSize = ScreenUtils.dp2px(100);

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
        mCropGridPaint.setColor(Color.parseColor("#80FFFFFF"));
        mCropGridPaint.setStrokeWidth(ScreenUtils.dp2px(1));
        mCropBorderPaint.setColor(Color.WHITE);
        mCropBorderPaint.setStrokeWidth(ScreenUtils.dp2px(1));
        mCropBorderPaint.setStyle(Paint.Style.STROKE);
        mCropBorderCornersPaint.setStrokeWidth(ScreenUtils.dp2px(3));
        mCropBorderCornersPaint.setColor(Color.WHITE);
        mCropBorderCornersPaint.setStyle(Paint.Style.STROKE);
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

    void setTargetCropRatio(float ratio) {
        mTargetCropRatio = ratio;
        setCropBounds();
        invalidate();
    }

    /**
     * 设置裁剪边界
     */
    void setCropBounds() {
        int height = (int) (mThisWidth / mTargetCropRatio);
        if (height > mThisHeight) {
            int width = (int) (mThisHeight * mTargetCropRatio);
            int halfDiff = (mThisWidth - width) / 2;
            mCropViewRect.set(getPaddingLeft() + halfDiff, getPaddingTop(),
                    getPaddingLeft() + width + halfDiff, getPaddingTop() + mThisHeight);
        } else {
            int halfDiff = (mThisHeight - height) / 2;
            mCropViewRect.set(getPaddingLeft(), getPaddingTop() + halfDiff,
                    getPaddingLeft() + mThisWidth, getPaddingTop() + height + halfDiff);
        }
        if (mModuleProxy != null) {
            mModuleProxy.onCropRectUpdated(mCropViewRect);
        }
    }

    void setIsHold(boolean isHold) {
        mIsHold = isHold;
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
                changeCropBounds(x, y);
                mPreviousTouchX = x;
                mPreviousTouchY = y;
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
                if (mTempRect.left + dx < getLeft() || mTempRect.right + dx > getRight()) {
                    dx = 0;
                }
                if (mTempRect.top + dy < getTop() || mTempRect.bottom + dy > getBottom()) {
                    dy = 0;
                }
                mTempRect.offset(dx, dy);     //位移
                if (dx != 0 || dy != 0) {
                    mCropViewRect.set(mTempRect);
                    invalidate();
                }
                return;
        }
        boolean changeHeight = mTempRect.height() >= mCropRectMinSize;
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
        canvas.drawColor(getResources().getColor(R.color.black_trans));
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
