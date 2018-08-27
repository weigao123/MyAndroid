package com.garfield.baselib.ui.crop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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

import java.util.Arrays;

/**
 * Created by gaowei3 on 2017/1/20.
 */

public class CropImageView extends TransformImageView {

    /**
     * 要裁剪的区域
     * mCropRect是以padding之后的居中的宽为mThisWidth，高为mThisHeight为父元素的相对坐标
     */
    private final RectF mCropRect = new RectF();

    private boolean mCropEnabled;
    private float mTargetCropRatio;    //裁剪框的比例

    private ModuleProxy mModuleProxy;

    private int mMaxResultImageSizeX = 0, mMaxResultImageSizeY = 0;

    private float mMaxScale, mMinScale;
    private float mMaxScaleMultiplier = 10;
    private final Matrix mTempMatrix = new Matrix();

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
        mCropRect.set(cropRect);
        mCropRect.offset(-getPaddingLeft(), -getPaddingTop());
        calculateImageScaleBounds();
        setImageToWrapCropBounds();
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

        if (mModuleProxy != null && mCropEnabled) {
            mModuleProxy.setTargetCropRatio(mTargetCropRatio, mCropRect);
        }
    }

    /**
     * 初始化放置图像
     */
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

    public void setCropEnabled(boolean cropEnabled) {
        mCropEnabled = cropEnabled;
    }

    public void setTargetCropRatio(float ratio) {
        mTargetCropRatio = ratio;
    }

    public void setMaxResultImageSize(@IntRange(from = 10) int maxResultImageSizeX, @IntRange(from = 10) int maxResultImageSizeY) {
        mMaxResultImageSizeX = maxResultImageSizeX;
        mMaxResultImageSizeY = maxResultImageSizeY;
    }

    public void cropAndSaveImage(@NonNull Bitmap.CompressFormat compressFormat, int compressQuality,
                                 @Nullable BitmapCropCallback cropCallback) {
        if (getImageOutputPath() == null) {
            return;
        }

        //cancelAllAnimations();
        setImageToWrapCropBounds(false);

        final ImageState imageState = new ImageState(
                mCropRect, RectUtils.trapToRect(mCurrentImageCorners),
                getCurrentScale(), getCurrentAngle());

        final CropParameters cropParameters = new CropParameters(
                mMaxResultImageSizeX, mMaxResultImageSizeY,
                compressFormat, compressQuality,
                getImageInputPath(), getImageOutputPath(), getExifInfo());

        new BitmapCropTask(mBitmapLoaded, imageState, cropParameters, cropCallback).execute();
    }

    public void zoomInImage(float scale, float centerX, float centerY) {
        if (scale <= mMaxScale) {
            postScale(scale / getCurrentScale(), centerX, centerY);
        }
    }

    /**
     * 计算，不懂。。。
     */
    private void calculateImageScaleBounds() {
        final Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        calculateImageScaleBounds(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    private void calculateImageScaleBounds(float drawableWidth, float drawableHeight) {
        float widthScale = Math.min(mCropRect.width() / drawableWidth, mCropRect.width() / drawableHeight);
        float heightScale = Math.min(mCropRect.height() / drawableHeight, mCropRect.height() / drawableWidth);

        mMinScale = Math.min(widthScale, heightScale);
        mMaxScale = mMinScale * mMaxScaleMultiplier;
    }

    /**
     * 移动加缩放，使裁剪框在图像内部
     */
    public void setImageToWrapCropBounds() {
        setImageToWrapCropBounds(false);
    }

    public void setImageToWrapCropBounds(boolean animate) {
        if (mCropEnabled && mBitmapLaidOut && !isImageWrapCropBounds()) {

            float currentX = mCurrentImageCenter[0];
            float currentY = mCurrentImageCenter[1];
            float currentScale = getCurrentScale();

            float deltaX = mCropRect.centerX() - currentX;
            float deltaY = mCropRect.centerY() - currentY;
            float deltaScale = 0;

            mTempMatrix.reset();
            mTempMatrix.setTranslate(deltaX, deltaY);

            final float[] tempCurrentImageCorners = Arrays.copyOf(mCurrentImageCorners, mCurrentImageCorners.length);

            // 把图像中心移动到裁剪框中心，这时候再判断isImageWrapCropBounds
            mTempMatrix.mapPoints(tempCurrentImageCorners);
            // 是否需要放大
            boolean willImageWrapCropBoundsAfterTranslate = isImageWrapCropBounds(tempCurrentImageCorners);

            if (willImageWrapCropBoundsAfterTranslate) {
                // 大小合适，只需要移动，但是不需要移动到中心点对齐，所以重新计算需要移动的距离
                final float[] imageIndents = calculateImageIndents();
                deltaX = -(imageIndents[0] + imageIndents[2]);    //左加右
                deltaY = -(imageIndents[1] + imageIndents[3]);    //上加下
            } else {
                // 大小不对，移动后(移动到中心点对齐)，必须再放大
                RectF tempCropRect = new RectF(mCropRect);
                mTempMatrix.reset();
                mTempMatrix.setRotate(getCurrentAngle());
                mTempMatrix.mapRect(tempCropRect);

                final float[] currentImageSides = RectUtils.getRectSidesFromCorners(mCurrentImageCorners);

                deltaScale = Math.max(tempCropRect.width() / currentImageSides[0],
                        tempCropRect.height() / currentImageSides[1]);
                deltaScale = deltaScale * currentScale - currentScale;
            }

            if (animate) {

            } else {
                // 移动完后检查是否需要再放大
                postTranslate(deltaX, deltaY);
                if (!willImageWrapCropBoundsAfterTranslate) {
                    zoomInImage(currentScale + deltaScale, mCropRect.centerX(), mCropRect.centerY());
                }
            }
        }
    }

    /**
     * 想象把手机反方向旋转后的效果，就理解了
     * true：裁剪框在图像内
     */
    protected boolean isImageWrapCropBounds() {
        return isImageWrapCropBounds(mCurrentImageCorners);
    }

    protected boolean isImageWrapCropBounds(float[] imageCorners) {
        mTempMatrix.reset();
        mTempMatrix.setRotate(-getCurrentAngle());

        // 图像和裁剪框同时反方向旋转
        float[] unRotatedImageCorners = Arrays.copyOf(imageCorners, imageCorners.length);
        mTempMatrix.mapPoints(unRotatedImageCorners);
        float[] unRotatedCropBoundsCorners = RectUtils.getCornersFromRect(mCropRect);
        mTempMatrix.mapPoints(unRotatedCropBoundsCorners);

        return RectUtils.trapToRect(unRotatedImageCorners).contains(RectUtils.trapToRect(unRotatedCropBoundsCorners));
    }

    /**
     * 1、旋转图像角点和裁剪角点，使图像矩形水平
     * 2、计算角点的边框差
     * 3、根据差值，得出是否需要用到这个差值(差值/0)，组合成4个值，想象成左上角和右下角的点
     */
    private float[] calculateImageIndents() {
        mTempMatrix.reset();
        mTempMatrix.setRotate(-getCurrentAngle());

        float[] unRotatedImageCorners = Arrays.copyOf(mCurrentImageCorners, mCurrentImageCorners.length);
        float[] unRotatedCropBoundsCorners = RectUtils.getCornersFromRect(mCropRect);

        mTempMatrix.mapPoints(unRotatedImageCorners);
        mTempMatrix.mapPoints(unRotatedCropBoundsCorners);

        RectF unRotatedImageRect = RectUtils.trapToRect(unRotatedImageCorners);
        RectF unRotatedCropRect = RectUtils.trapToRect(unRotatedCropBoundsCorners);

        float deltaLeft = unRotatedImageRect.left - unRotatedCropRect.left;
        float deltaTop = unRotatedImageRect.top - unRotatedCropRect.top;
        float deltaRight = unRotatedImageRect.right - unRotatedCropRect.right;
        float deltaBottom = unRotatedImageRect.bottom - unRotatedCropRect.bottom;

        float indents[] = new float[4];
        indents[0] = (deltaLeft > 0) ? deltaLeft : 0;   // 正值说明裁剪在图像外面，要用到这个差值，返回正
        indents[1] = (deltaTop > 0) ? deltaTop : 0;
        indents[2] = (deltaRight < 0) ? deltaRight : 0;   // 负值说明裁剪在图像外面，要用到这个差值，返回负
        indents[3] = (deltaBottom < 0) ? deltaBottom : 0;

        mTempMatrix.reset();
        mTempMatrix.setRotate(getCurrentAngle());
        mTempMatrix.mapPoints(indents);

        return indents;
    }



}
