package com.garfield.weishu.setting;

import android.widget.ImageView;

import com.garfield.baselib.utils.ImageLoaderUtils;
import com.garfield.weishu.R;
import com.garfield.weishu.base.listview.TListViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by gaowei3 on 2016/10/19.
 */

public class PhotoListViewHolder extends TListViewHolder<String> {

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
