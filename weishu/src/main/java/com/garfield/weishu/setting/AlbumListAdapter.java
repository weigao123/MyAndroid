package com.garfield.weishu.setting;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.garfield.baselib.utils.drawable.PhotoUtil;
import com.garfield.baselib.utils.http.image.ImageHelper;
import com.garfield.weishu.R;
import com.garfield.weishu.base.listview.BaseListAdapter;
import com.garfield.weishu.base.listview.BaseListViewHolder;

import java.util.List;

/**
 * Created by gaowei3 on 2016/10/21.
 */

public class AlbumListAdapter extends BaseListAdapter<PhotoUtil.AlbumInfo> {

    private int albumSelect;

    public AlbumListAdapter(Context context, List<PhotoUtil.AlbumInfo> items) {
        super(context, items);
    }

    @Override
    public Class getViewHolderClassAtPosition(int position) {
        return AlbumListViewHolder.class;
    }

    @Override
    public int getViewHolderCount() {
        return 1;
    }

    public int getAlbumSelect() {
        return albumSelect;
    }

    public void setAlbumSelect(int albumSelect) {
        this.albumSelect = albumSelect;
    }


    public static class AlbumListViewHolder extends BaseListViewHolder<PhotoUtil.AlbumInfo> {

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
        protected void refresh(PhotoUtil.AlbumInfo item) {
            mAlbumName.setText(item.albumName);
            mImageCount.setText(getAdapter().getContext().getString(R.string.photo_count, item.photoPaths.size()+""));
            AlbumListAdapter adapter = getAdapter();
            if (mPosition == adapter.getAlbumSelect()) {
                mChoose.setVisibility(View.VISIBLE);
            } else {
                mChoose.setVisibility(View.GONE);
            }
            ImageHelper.load(mRootView.getContext(), "file://" + item.albumImage, mAlbumImage);
        }


    }
}
