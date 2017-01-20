package com.garfield.baselib.ui.widget.CropImage;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.garfield.baselib.R;

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
    }


    @Override
    public void setCropAspectRatio(float aspectRatio) {
        mOverlayView.setTargetAspectRatio(aspectRatio);
    }

    @Override
    public void setCropRect(RectF rectF) {

    }
}
