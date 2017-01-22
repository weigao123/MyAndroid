package com.garfield.baselib.ui.widget.CropImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by gaowei3 on 2017/1/20.
 */

public class CropImageView extends GestureImageView {

    private final RectF mCropRect = new RectF();

    private float mTargetAspectRatio;
    private ModuleProxy mModuleProxy;

    public CropImageView(Context context) {
        this(context, null);
    }

    public CropImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void setModuleProxy(ModuleProxy moduleProxy) {
        mModuleProxy = moduleProxy;
    }

    public void setCropRect(RectF cropRect) {

    }

    @Override
    protected void onImageLaidOut() {
        super.onImageLaidOut();
        final Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        float drawableWidth = drawable.getIntrinsicWidth();
        float drawableHeight = drawable.getIntrinsicHeight();

        mTargetAspectRatio = drawableWidth / drawableHeight;

        int height = (int) (mThisWidth / mTargetAspectRatio);
        if (height > mThisHeight) {
            // 瘦高
            int width = (int) (mThisHeight * mTargetAspectRatio);
            int halfDiff = (mThisWidth - width) / 2;
            mCropRect.set(halfDiff, 0, width + halfDiff, mThisHeight);
        } else {
            // 矮胖
            int halfDiff = (mThisHeight - height) / 2;
            mCropRect.set(0, halfDiff, mThisWidth, height + halfDiff);
        }
        setupInitialImagePosition(drawableWidth, drawableHeight);

        if (mModuleProxy != null) {
            mModuleProxy.setCropAspectRatio(mTargetAspectRatio);
        }
    }

    private void setupInitialImagePosition(float drawableWidth, float drawableHeight) {
        float widthScale = mCropRect.width() / drawableWidth;
        float heightScale = mCropRect.height() / drawableHeight;
        float maxScale = Math.max(widthScale, heightScale);    //保证整个Image都在容器内

        float xDiff = mCropRect.centerX() - (drawableWidth * maxScale) / 2.0f;   //目标位置和图像缩小后的中心点之差，就是要移动的距离
        float yDiff = mCropRect.centerY() - (drawableHeight * maxScale) / 2.0f;

        mCurrentImageMatrix.reset();
        mCurrentImageMatrix.postScale(maxScale, maxScale);    //宽高都按一样的尺寸缩放
        mCurrentImageMatrix.postTranslate(xDiff, yDiff);      //tw的计算前提是，先缩小，再移动
        setImageMatrix(mCurrentImageMatrix);
    }

    public void cropAndSaveImage(@NonNull Bitmap.CompressFormat compressFormat, int compressQuality,
                                 @Nullable BitmapCropCallback cropCallback) {

        final ImageState imageState = new ImageState(
                mCropRect, RectUtils.trapToRect(mCurrentImageCorners),
                getCurrentScale(), getCurrentAngle());

        final CropParameters cropParameters = new CropParameters(
                mMaxResultImageSizeX, mMaxResultImageSizeY,
                compressFormat, compressQuality,
                getImageInputPath(), getImageOutputPath(), getExifInfo());

        new BitmapCropTask(getViewBitmap(), imageState, cropParameters, cropCallback).execute();
    }
}
