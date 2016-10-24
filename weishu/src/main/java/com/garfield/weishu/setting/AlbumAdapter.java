package com.garfield.weishu.setting;

import android.content.Context;

import com.garfield.baselib.utils.PhotoUtil;
import com.garfield.weishu.base.adapter.TAdapter;
import com.garfield.weishu.base.adapter.TAdapterDelegate;

import java.util.List;

/**
 * Created by gaowei3 on 2016/10/21.
 */

public class AlbumAdapter extends TAdapter<PhotoUtil.AlbumInfo> {

    private int choosePosition;

    public AlbumAdapter(Context context, List<PhotoUtil.AlbumInfo> items, TAdapterDelegate delegate) {
        super(context, items, delegate);
    }

    public int getChoosePosition() {
        return choosePosition;
    }

    public void setChoosePosition(int choosePosition) {
        this.choosePosition = choosePosition;
    }
}
