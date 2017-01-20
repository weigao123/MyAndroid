package com.garfield.baselib.ui.widget.CropImage;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.garfield.baselib.utils.system.L;

/**
 * Created by gaowei3 on 2017/1/19.
 */

public class GestureImageView extends ImageView {

    private static final int MATRIX_VALUES_COUNT = 9;

    protected Matrix mCurrentImageMatrix = new Matrix();

    protected boolean mBitmapDecoded = false;
    protected boolean mBitmapLaidOut = false;

    protected int mThisWidth, mThisHeight;

    private float[] mInitialImageCorners;    //4个角点
    private float[] mInitialImageCenter;     //1个中心点
    protected final float[] mCurrentImageCorners = new float[8];
    protected final float[] mCurrentImageCenter = new float[2];

    public GestureImageView(Context context) {
        this(context, null);
    }

    public GestureImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 必须被设置成MATRIX
        setScaleType(ScaleType.MATRIX);
    }

    public void setImageUrl(String url) {
        Glide.with(this.getContext().getApplicationContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        mBitmapDecoded = true;
                        setImageDrawable(resource);
                    }
                });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed || (mBitmapDecoded && !mBitmapLaidOut)) {
            mThisWidth = getWidth() - getPaddingRight() - getPaddingLeft();
            mThisHeight = getHeight() - getPaddingBottom() - getPaddingTop();
            onImageLaidOut();
        }
    }

    /**
     * Image是这个View的子元素，增加了一个子元素所以会重新requestLayout()
     */
    protected void onImageLaidOut() {
        final Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        mBitmapLaidOut = true;

        float w = drawable.getIntrinsicWidth();
        float h = drawable.getIntrinsicHeight();
        L.d("w: " + w + ", h: " + h);

        // 不做任何变换的时候位置
        RectF initialImageRect = new RectF(0, 0, w, h);
        mInitialImageCorners = RectUtils.getCornersFromRect(initialImageRect);
        mInitialImageCenter = RectUtils.getCenterFromRect(initialImageRect);

    }

    /**
     * 移动
     */
    public void postTranslate(float deltaX, float deltaY) {
        if (deltaX != 0 || deltaY != 0) {
            mCurrentImageMatrix.postTranslate(deltaX, deltaY);
            setImageMatrix(mCurrentImageMatrix);
        }
    }

    /**
     * 缩放
     */
    public void postScale(float deltaScale, float px, float py) {
        if (deltaScale != 0) {
            mCurrentImageMatrix.postScale(deltaScale, deltaScale, px, py);
            setImageMatrix(mCurrentImageMatrix);
        }
    }

    /**
     * 旋转
     */
    public void postRotate(float deltaAngle, float px, float py) {
        if (deltaAngle != 0) {
            mCurrentImageMatrix.postRotate(deltaAngle, px, py);
            setImageMatrix(mCurrentImageMatrix);
        }
    }


    @Override
    public void setImageMatrix(Matrix matrix) {
        super.setImageMatrix(matrix);
        mCurrentImageMatrix.set(matrix);
        updateCurrentImagePoints();
    }

    /**
     * 根据初始任意点的坐标(x, y)，以及变换矩阵，生成新的位置坐标
     */
    private void updateCurrentImagePoints() {
        mCurrentImageMatrix.mapPoints(mCurrentImageCorners, mInitialImageCorners);
        mCurrentImageMatrix.mapPoints(mCurrentImageCenter, mInitialImageCenter);
    }
}
