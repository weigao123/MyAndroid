package com.garfield.weishu.setting;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.garfield.baselib.utils.drawable.PhotoUtil;
import com.garfield.baselib.utils.http.image.ImageHelper;
import com.garfield.weishu.R;
import com.garfield.weishu.base.listview.BaseListViewHolder;

/**
 * Created by gaowei3 on 2016/10/21.
 */

public class AlbumListViewHolder extends BaseListViewHolder<PhotoUtil.AlbumInfo> {

    private ImageView mAlbumImage;
    private TextView mAlbumName;
    private TextView mImageCount;
    private ImageView mChoose;

    @Override
    protected int getResId() {
        return R.layout.item_album;
    }

    @Override
    protected void inflateView() {
        mAlbumImage = findView(R.id.item_album_image);
        mAlbumName = findView(R.id.item_album_name);
        mImageCount = findView(R.id.item_album_count);
        mChoose = findView(R.id.item_album_choose);
    }

    @Override
    public void setView() {

    }

    @Override
    protected void refresh(PhotoUtil.AlbumInfo item) {
        mAlbumName.setText(item.albumName);
        mImageCount.setText(getAdapter().getContext().getString(R.string.photo_count, item.photoPaths.size()+""));
        if (mPosition == getAdapter().getAlbumSelect()) {
            mChoose.setVisibility(View.VISIBLE);
        } else {
            mChoose.setVisibility(View.GONE);
        }
        ImageHelper.load(mRootView.getContext(), "file://" + item.albumImage, mAlbumImage);
    }

    @Override
    protected AlbumListAdapter getAdapter() {
        return (AlbumListAdapter)mAdapter;
    }


}
