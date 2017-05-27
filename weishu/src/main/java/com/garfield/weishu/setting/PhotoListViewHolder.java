package com.garfield.weishu.setting;

import android.widget.ImageView;

import com.garfield.baselib.utils.http.image.ImageHelper;
import com.garfield.weishu.R;
import com.garfield.weishu.base.listview.BaseListViewHolder;

/**
 * Created by gaowei3 on 2016/10/19.
 */

public class PhotoListViewHolder extends BaseListViewHolder<String> {

    private ImageView mImageView;

    @Override
    protected int getResId() {
        return R.layout.item_take_photo;
    }

    @Override
    protected void inflateView() {
        mImageView = findView(R.id.item_take_photo_image);
    }

    @Override
    public void setView() {

    }

    @Override
    protected void refresh(String item) {
        if ("Camera".equals(item)) {
            mImageView.setImageResource(R.drawable.ic_camera_pressed);
            return;
        }
        ImageHelper.load(mRootView.getContext(), "file://" + item, mImageView);

    }

    @Override
    protected PhotoListAdapter getAdapter() {
        return (PhotoListAdapter)mAdapter;
    }
}
