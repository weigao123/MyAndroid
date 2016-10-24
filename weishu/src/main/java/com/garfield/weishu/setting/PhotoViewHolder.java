package com.garfield.weishu.setting;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.garfield.baselib.utils.ImageLoaderUtils;
import com.garfield.baselib.utils.L;
import com.garfield.weishu.R;
import com.garfield.weishu.base.adapter.TViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by gaowei3 on 2016/10/19.
 */

public class PhotoViewHolder extends TViewHolder<String> {

    private ImageView mImageView;

    @Override
    protected int getResId() {
        return R.layout.item_take_photo;
    }

    @Override
    protected void inflateChildView() {
        mImageView = findView(R.id.item_take_photo_image);
    }

    @Override
    protected void refresh(String item) {
        if ("Camera".equals(item)) {
            mImageView.setImageResource(R.drawable.ic_camera_pressed);
            return;
        }
        ImageLoader.getInstance().displayImage("file://" + item, mImageView, ImageLoaderUtils.getDisplayImageNoDiskCacheOptions());
    }
}
