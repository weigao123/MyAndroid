package com.garfield.baselib.ui.crop.view;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.garfield.baselib.R;
import com.garfield.baselib.ui.crop.CropParams;
import com.garfield.baselib.ui.crop.callback.ModuleProxy;

/**
 * Created by gaowei3 on 2017/1/20.
 */

public class MyCropView extends FrameLayout implements ModuleProxy {

    private GestureCropImageView mGestureCropImageView;
    private OverlayView mOverlayView;

    public MyCropView(Context context) {
        this(context, null);
    }

    public MyCropView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.widget_crop_image, this);
        mGestureCropImageView = (GestureCropImageView) findViewById(R.id.widget_crop_image);
        mGestureCropImageView.setModuleProxy(this);
        mOverlayView = (OverlayView) findViewById(R.id.widget_crop_image_overlay);
        mOverlayView.setModuleProxy(this);
    }

    public void setCropParams(CropParams cropParams) {
        mGestureCropImageView.setImageUrl(cropParams.getInputUri(), cropParams.getOutputUri());
        mGestureCropImageView.setMaxResultImageSize(cropParams.getMaxSizeX(), cropParams.getMaxSizeY());
        mGestureCropImageView.setCropEnabled(cropParams.getCropEnable());
        mGestureCropImageView.setScaleEnabled(cropParams.getScaleEnable());
        mGestureCropImageView.setRotateEnabled(cropParams.getRotateEnable());
        mGestureCropImageView.setTargetCropRatio(cropParams.getCropRatio());
        mOverlayView.setHoldEnabled(cropParams.getHoldEnable());
    }

    public void setTransformListener(TransformImageView.TransformImageListener listener) {
        mGestureCropImageView.setTransformImageListener(listener);
    }

    @Override
    public void setTargetCropRatio(float aspectRatio, RectF imgRect) {
        mOverlayView.setTargetCropRatio(aspectRatio, imgRect);
    }

    @Override
    public void onCropRectUpdated(RectF rectF) {
        mGestureCropImageView.setCropRect(rectF);
    }

    public GestureCropImageView getGestureCropImageView() {
        return mGestureCropImageView;
    }

    public OverlayView getOverlayView() {
        return mOverlayView;
    }
}
