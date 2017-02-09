package com.garfield.baselib.ui.crop;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * Created by Oleksii Shliama (https://github.com/shliama).
 * <p/>
 * Builder class to ease Intent setup.
 */
public class CropParams implements Parcelable {

    public static final int REQUEST_CROP = 69;
    public static final int RESULT_ERROR = 96;

    private static final String PHOTO_INPUT_URI = "input_path";
    private static final String PHOTO_OUTPUT_URI = "output_path";
    // 是否显示裁剪框
    private static final String CROP_ENABLE = "crop_enable";
    // 裁剪框初始化的比例
    private static final String CROP_RATIO = "ratio";
    // 是否固定这个裁剪比例
    private static final String HOLD_ENABLE = "hold_enable";
    // 是否允许缩放
    private static final String SCALE_ENABLE = "scale_enable";
    // 是否允许旋转
    private static final String ROTATE_ENABLE = "rotate_enable";
    // 最大裁剪后的像素
    private static final String MAX_SIZE_X = "size_x";
    private static final String MAX_SIZE_Y = "size_y";

    private Bundle mCropOptionsBundle;

    private CropParams() {

    }

    public static CropParams of(@NonNull Uri source) {
        return new CropParams(source);
    }

    private CropParams(@NonNull Uri source) {
        mCropOptionsBundle = new Bundle();
        mCropOptionsBundle.putParcelable(PHOTO_INPUT_URI, source);
    }

    public CropParams withOutputUri(Uri outputUri) {
        mCropOptionsBundle.putParcelable(PHOTO_OUTPUT_URI, outputUri);
        return this;
    }

    public CropParams withCropEnable(boolean cropEnable) {
        mCropOptionsBundle.putBoolean(CROP_ENABLE, cropEnable);
        return this;
    }

    public CropParams withCropRatio(float ratio) {
        mCropOptionsBundle.putFloat(CROP_RATIO, ratio);
        return this;
    }

    public CropParams withHoldEnable(boolean holdEnable) {
        mCropOptionsBundle.putBoolean(HOLD_ENABLE, holdEnable);
        return this;
    }

    public CropParams withScaleEnable(boolean scaleEnable) {
        mCropOptionsBundle.putBoolean(SCALE_ENABLE, scaleEnable);
        return this;
    }

    public CropParams withRotateEnable(boolean rotateEnable) {
        mCropOptionsBundle.putBoolean(ROTATE_ENABLE, rotateEnable);
        return this;
    }

    public CropParams withMaxResultSize(@IntRange(from = 100) int width, int height) {
        mCropOptionsBundle.putInt(MAX_SIZE_X, width);
        mCropOptionsBundle.putInt(MAX_SIZE_Y, height);
        return this;
    }

    public Uri getInputUri() {
        return mCropOptionsBundle.getParcelable(PHOTO_INPUT_URI);
    }

    public Uri getOutputUri() {
        return mCropOptionsBundle.getParcelable(PHOTO_OUTPUT_URI);
    }

    public boolean getCropEnable() {
        return mCropOptionsBundle.getBoolean(CROP_ENABLE);
    }

    public boolean getScaleEnable() {
        return mCropOptionsBundle.getBoolean(SCALE_ENABLE);
    }

    public boolean getRotateEnable() {
        return mCropOptionsBundle.getBoolean(ROTATE_ENABLE);
    }

    public float getCropRatio() {
        return mCropOptionsBundle.getFloat(CROP_RATIO);
    }

    public boolean getHoldEnable() {
        return mCropOptionsBundle.getBoolean(HOLD_ENABLE);
    }

    public int getMaxSizeX() {
        return mCropOptionsBundle.getInt(MAX_SIZE_X);
    }

    public int getMaxSizeY() {
        return mCropOptionsBundle.getInt(MAX_SIZE_Y);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mCropOptionsBundle, flags);
    }

    public static final Parcelable.Creator<CropParams> CREATOR = new Parcelable.Creator<CropParams>() {
        public CropParams createFromParcel(Parcel source) {
            CropParams cropParams = new CropParams();
            cropParams.mCropOptionsBundle = source.readParcelable(CropParams.class.getClassLoader());
            return cropParams;
        }
        public CropParams[] newArray(int size) {
            return new CropParams[size];
        }
    };
}
