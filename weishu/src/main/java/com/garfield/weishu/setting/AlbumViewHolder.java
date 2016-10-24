package com.garfield.weishu.setting;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.garfield.baselib.utils.ImageLoaderUtils;
import com.garfield.baselib.utils.PhotoUtil;
import com.garfield.weishu.R;
import com.garfield.weishu.base.adapter.TViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by gaowei3 on 2016/10/21.
 */

public class AlbumViewHolder extends TViewHolder<PhotoUtil.AlbumInfo> {

    private ImageView mAlbumImage;
    private TextView mAlbumName;
    private TextView mImageCount;
    private ImageView mChoose;

    @Override
    protected int getResId() {
        return R.layout.item_album;
    }

    @Override
    protected void inflateChildView() {
        mAlbumImage = findView(R.id.item_album_image);
        mAlbumName = findView(R.id.item_album_name);
        mImageCount = findView(R.id.item_album_count);
        mChoose = findView(R.id.item_album_choose);
    }

    @Override
    protected void refresh(PhotoUtil.AlbumInfo item) {
        mAlbumName.setText(item.albumName);
        mImageCount.setText(getAdapter().getContext().getString(R.string.photo_count, item.photoPaths.size()+""));
        if (mPosition == ((AlbumAdapter)getAdapter()).getChoosePosition()) {
            mChoose.setVisibility(View.VISIBLE);
        } else {
            mChoose.setVisibility(View.GONE);
        }
        ImageLoader.getInstance().displayImage("file://" + item.albumImage, mAlbumImage, ImageLoaderUtils.getDisplayImageOptions());
    }
}
