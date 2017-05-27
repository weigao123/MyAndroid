package com.garfield.weishu.setting;

import android.content.Context;

import com.garfield.baselib.utils.drawable.PhotoUtil;
import com.garfield.weishu.base.listview.BaseListAdapter;

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
}
