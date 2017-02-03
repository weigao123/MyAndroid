package com.garfield.baselib.ui.crop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.garfield.baselib.ui.crop.callback.BitmapCropCallback;
import com.garfield.baselib.ui.crop.callback.ModuleProxy;
import com.garfield.baselib.ui.crop.model.CropParameters;
import com.garfield.baselib.ui.crop.model.ImageState;
import com.yalantis.ucrop.task.BitmapCropTask;
import com.garfield.baselib.ui.crop.utils.RectUtils;

/**
 * Created by gaowei3 on 2017/1/20.
 */

public class CropImageView extends TransformImageView {

    /**
     * 要裁剪的区域
     * mCropRect是以padding之后的居中的宽为mThisWidth，高为mThisHeight为父元素的相对坐标
     */
    private final RectF mCropRect = new RectF();

    private boolean mIsCrop;
    private float mTargetCropRatio;    //裁剪框的比例

    private ModuleProxy mModuleProxy;

    private int mMaxResultImageSizeX = 0, mMaxResultImageSizeY = 0;

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

    /**
     * 参数是包含了padding的Rect
     */
    public void setCropRect(RectF cropRect) {
        mTargetCropRatio = cropRect.width() / cropRect.height();
        mCropRect.set(cropRect.left - getPaddingLeft(), cropRect.top - getPaddingTop(),
                cropRect.right - getPaddingRight(), cropRect.bottom - getPaddingBottom());

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

        float aspectRatio = drawableWidth / drawableHeight;
        if (mTargetCropRatio == 0) {
            mTargetCropRatio = aspectRatio;
        }

        /**
         * 因为setImageBitmap后图像其实就已经向右下位移padding了，这时才是初始位置
         * mCurrentImageRect是以图像初始位置为基准，对准图像的左上角，右下角
         * mCropRect最好也以图像初始位置为基准，所以mCropRect以(paddingLeft, paddingTop)坐标为基准
         * 根本是为了方便裁剪时crop(float resizeScale)方法里直接相减，确定left和top坐标，两个Rect都以同样的坐标为基准
         *
         * 先根据Image尺寸设置mCropRect，默认区域是整个完整的图像，有一个边长是mThisWidth或者mThisHeight
         * 需要根据这个计算初始变换Matrix
         */
        int height = (int) (mThisWidth / aspectRatio);
        if (height > mThisHeight) {
            // 瘦高，左右留空白
            int width = (int) (mThisHeight * aspectRatio);  //图片高度顶框缩放后的宽度
            int halfDiff = (mThisWidth - width) / 2;
            /**
             * mCropRect以(paddingLeft, paddingTop)坐标为基准的相对坐标
             */
            mCropRect.set(halfDiff, 0, width + halfDiff, mThisHeight);
        } else {
            // 矮胖，上下留空白
            int halfDiff = (mThisHeight - height) / 2;
            mCropRect.set(0, halfDiff, mThisWidth, height + halfDiff);
        }
        initialImagePosition(drawableWidth, drawableHeight);

        if (mModuleProxy != null && mIsCrop) {
            mModuleProxy.setTargetCropRatio(mTargetCropRatio);
        }
    }

    private void initialImagePosition(float drawableWidth, float drawableHeight) {
        float widthScale = mCropRect.width() / drawableWidth;
        float heightScale = mCropRect.height() / drawableHeight;
        float maxScale = Math.max(widthScale, heightScale);    //用最大的比例，保证整个Image都在容器内
        // 目标位置和图像缩小后的中心点之差，就是要移动的距离
        // 都是相对坐标，可以直接减
        float xDiff = mCropRect.centerX() - (drawableWidth * maxScale) / 2.0f;
        float yDiff = mCropRect.centerY() - (drawableHeight * maxScale) / 2.0f;

        mCurrentImageMatrix.reset();
        mCurrentImageMatrix.postScale(maxScale, maxScale);    //以(0,0)点为中心，保持比例缩放
        mCurrentImageMatrix.postTranslate(xDiff, yDiff);      //计算前提是，先缩小，再移动
        setImageMatrix(mCurrentImageMatrix);
    }

    public void setEnableCrop(boolean isCrop) {
        mIsCrop = isCrop;
    }

    public void setTargetCropRatio(float ratio) {
        mTargetCropRatio = ratio;
    }

    public void setMaxResultImageSizeX(@IntRange(from = 10) int maxResultImageSizeX) {
        mMaxResultImageSizeX = maxResultImageSizeX;
    }

    public void setMaxResultImageSizeY(@IntRange(from = 10) int maxResultImageSizeY) {
        mMaxResultImageSizeY = maxResultImageSizeY;
    }

    public void cropAndSaveImage(@NonNull Bitmap.CompressFormat compressFormat, int compressQuality,
                                 @Nullable BitmapCropCallback cropCallback) {
//        cancelAllAnimations();
//        setImageToWrapCropBounds(false);
//
        final ImageState imageState = new ImageState(
                mCropRect, RectUtils.trapToRect(mCurrentImageCorners),
                getCurrentScale(), getCurrentAngle());

        final CropParameters cropParameters = new CropParameters(
                mMaxResultImageSizeX, mMaxResultImageSizeY,
                compressFormat, compressQuality,
                getImageInputPath(), getImageOutputPath(), getExifInfo());

        new BitmapCropTask(mBitmap, imageState, cropParameters, cropCallback).execute();
    }
}
