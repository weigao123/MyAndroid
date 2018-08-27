package com.garfield.baselib.ui.crop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.garfield.baselib.ui.crop.callback.BitmapLoadCallback;
import com.garfield.baselib.ui.crop.model.ExifInfo;
import com.garfield.baselib.ui.crop.utils.BitmapLoadUtils;
import com.garfield.baselib.ui.crop.utils.RectUtils;
import com.garfield.baselib.utils.system.L;

import java.util.Locale;

/**
 * Created by gaowei3 on 2017/1/19.
 */

public class TransformImageView extends ImageView {

    private static final int MATRIX_VALUES_COUNT = 9;

    protected Matrix mCurrentImageMatrix = new Matrix();
    private final float[] mMatrixValues = new float[MATRIX_VALUES_COUNT];

    protected boolean mBitmapDecoded = false;
    protected boolean mBitmapLaidOut = false;

    protected int mThisWidth, mThisHeight;    //去掉padding后的尺寸

    private float[] mInitialImageCorners;    //不做任何变换时的4个角点
    private float[] mInitialImageCenter;     //不做任何变换时的1个中心点
    protected final float[] mCurrentImageCorners = new float[8];   //变换后，当前的4个角点坐标
    protected final float[] mCurrentImageCenter = new float[2];   //变换后，当前的1个中心点坐标

    protected TransformImageListener mTransformImageListener;

    private int mMaxBitmapSize = 0;
    private String mImageInputPath, mImageOutputPath;
    private ExifInfo mExifInfo;

    protected Bitmap mBitmapLoaded;

    public TransformImageView(Context context) {
        this(context, null);
    }

    public TransformImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransformImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 必须被设置成MATRIX
        setScaleType(ScaleType.MATRIX);
    }

    public interface TransformImageListener {
        void onLoadComplete();
        void onLoadFailure(@NonNull Exception e);
        void onRotate(float currentAngle);
        void onScale(float currentScale);
    }

    public void setTransformImageListener(TransformImageListener transformImageListener) {
        mTransformImageListener = transformImageListener;
    }

    public void setImageUrl(Uri imageUri, Uri outputUri) {
        int maxBitmapSize = getMaxBitmapSize();

        BitmapLoadUtils.decodeBitmapInBackground(getContext(), imageUri, outputUri, maxBitmapSize, maxBitmapSize,
                new BitmapLoadCallback() {

                    @Override
                    public void onBitmapLoaded(@NonNull Bitmap bitmap, @NonNull ExifInfo exifInfo, @NonNull String imageInputPath, @Nullable String imageOutputPath) {
                        mBitmapLoaded = bitmap;
                        mImageInputPath = imageInputPath;
                        mImageOutputPath = imageOutputPath;
                        mExifInfo = exifInfo;

                        mBitmapDecoded = true;
                        setImageBitmap(bitmap);
                    }

                    @Override
                    public void onFailure(@NonNull Exception bitmapWorkerException) {
                        if (mTransformImageListener != null) {
                            mTransformImageListener.onLoadFailure(bitmapWorkerException);
                        }
                    }
                });
    }

    public void setMaxBitmapSize(int maxBitmapSize) {
        mMaxBitmapSize = maxBitmapSize;
    }

    public int getMaxBitmapSize() {
        if (mMaxBitmapSize <= 0) {
            mMaxBitmapSize = BitmapLoadUtils.calculateMaxBitmapSize(getContext());
        }
        return mMaxBitmapSize;
    }

    public String getImageInputPath() {
        return mImageInputPath;
    }

    public String getImageOutputPath() {
        return mImageOutputPath;
    }

    public ExifInfo getExifInfo() {
        return mExifInfo;
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

        float w = drawable.getIntrinsicWidth();
        float h = drawable.getIntrinsicHeight();

        L.d(String.format(Locale.getDefault(), "Image size: [%d:%d]", (int) w, (int) h));

        // 不做任何变换的时候位置，其实图像已经padding移动过了
        // 所以以(paddingLeft, paddingTop)坐标为基准
        RectF initialImageRect = new RectF(0, 0, w, h);
        mInitialImageCorners = RectUtils.getCornersFromRect(initialImageRect);
        mInitialImageCenter = RectUtils.getCenterFromRect(initialImageRect);

        mBitmapLaidOut = true;

        if (mTransformImageListener != null) {
            mTransformImageListener.onLoadComplete();
        }
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
            if (mTransformImageListener != null) {
                mTransformImageListener.onScale(getMatrixScale(mCurrentImageMatrix));
            }
        }
    }

    /**
     * 旋转
     */
    public void postRotate(float deltaAngle, float px, float py) {
        if (deltaAngle != 0) {
            mCurrentImageMatrix.postRotate(deltaAngle, px, py);
            setImageMatrix(mCurrentImageMatrix);
            if (mTransformImageListener != null) {
                mTransformImageListener.onRotate(getMatrixAngle(mCurrentImageMatrix));
            }
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

    public float getCurrentScale() {
        return getMatrixScale(mCurrentImageMatrix);
    }

    public float getMatrixScale(@NonNull Matrix matrix) {
        return (float) Math.sqrt(Math.pow(getMatrixValue(matrix, Matrix.MSCALE_X), 2)
                + Math.pow(getMatrixValue(matrix, Matrix.MSKEW_Y), 2));
    }

    public float getCurrentAngle() {
        return getMatrixAngle(mCurrentImageMatrix);
    }

    public float getMatrixAngle(@NonNull Matrix matrix) {
        return (float) -(Math.atan2(getMatrixValue(matrix, Matrix.MSKEW_X),
                getMatrixValue(matrix, Matrix.MSCALE_X)) * (180 / Math.PI));
    }

    protected float getMatrixValue(@NonNull Matrix matrix, @IntRange(from = 0, to = MATRIX_VALUES_COUNT) int valueIndex) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[valueIndex];
    }
}
