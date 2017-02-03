package com.garfield.baselib.ui.crop.callback;

import android.graphics.RectF;

/**
 * Created by gaowei3 on 2017/1/20.
 */

public interface ModuleProxy {
    void setTargetCropRatio(float aspectRatio);
    void onCropRectUpdated(RectF rectF);
}
