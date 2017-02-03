package com.garfield.baselib.ui.crop;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Oleksii Shliama (https://github.com/shliama).
 * <p/>
 * Builder class to ease Intent setup.
 */
public class CropParams implements Serializable {

    public static final int REQUEST_CROP = 69;
    public static final int RESULT_ERROR = 96;

    public static final String PHOTO_INPUT_URI = "input_path";
    public static final String PHOTO_OUTPUT_URI = "output_path";
    public static final String IS_CROP = "is_crop";
    public static final String CROP_RATIO = "ratio";
    public static final String IS_HOLD = "hold";
    public static final String MAX_SIZE_X = "size_x";
    public static final String MAX_SIZE_Y = "size_y";

    private Bundle mCropOptionsBundle;

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

    public CropParams withIsCrop(boolean isCrop) {
        mCropOptionsBundle.putBoolean(IS_CROP, isCrop);
        return this;
    }

    public CropParams withCropRatio(float ratio) {
        mCropOptionsBundle.putFloat(CROP_RATIO, ratio);
        return this;
    }

    public CropParams withIsHold(boolean isHold) {
        mCropOptionsBundle.putBoolean(IS_HOLD, isHold);
        return this;
    }

    public CropParams withMaxResultSize(@IntRange(from = 100) int width, int height) {
        mCropOptionsBundle.putInt(MAX_SIZE_X, width);
        mCropOptionsBundle.putInt(MAX_SIZE_Y, height);
        return this;
    }


    public Bundle getCropOptionsBundle() {
        return mCropOptionsBundle;
    }

    public Uri getInputUri() {
        return mCropOptionsBundle.getParcelable(PHOTO_INPUT_URI);
    }

    public Uri getOutputUri() {
        return mCropOptionsBundle.getParcelable(PHOTO_OUTPUT_URI);
    }

    public boolean getIsCrop() {
        return mCropOptionsBundle.getBoolean(IS_CROP);
    }

    public float getCropRatio() {
        return mCropOptionsBundle.getFloat(CROP_RATIO);
    }

    public boolean getIsHold() {
        return mCropOptionsBundle.getBoolean(IS_HOLD);
    }

    public int getMaxSizeX() {
        return mCropOptionsBundle.getInt(MAX_SIZE_X);
    }

    public int getMaxSizeY() {
        return mCropOptionsBundle.getInt(MAX_SIZE_Y);
    }

}
