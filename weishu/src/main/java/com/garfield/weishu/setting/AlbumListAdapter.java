package com.garfield.weishu.setting;

import android.content.Context;

import com.garfield.baselib.utils.PhotoUtil;
import com.garfield.weishu.base.listview.TListAdapter;
import com.garfield.weishu.base.listview.TListAdapterDelegate;

import java.util.List;

/**
 * Created by gaowei3 on 2016/10/21.
 */

public class AlbumListAdapter extends TListAdapter<PhotoUtil.AlbumInfo> {

    private int albumSelect;

    public AlbumListAdapter(Context context, List<PhotoUtil.AlbumInfo> items, TListAdapterDelegate delegate) {
        super(context, items, delegate);
    }

    public int getAlbumSelect() {
        return albumSelect;
    }

    public void setAlbumSelect(int albumSelect) {
        this.albumSelect = albumSelect;
    }
}
