package com.garfield.weishu.setting;

import android.content.Context;

import com.garfield.weishu.base.listview.BaseListAdapter;

import java.util.List;

/**
 * Created by gaowei3 on 2016/10/19.
 */

public class PhotoListAdapter extends BaseListAdapter<String> {


    public PhotoListAdapter(Context context, List<String> items) {
        super(context, items);
    }

    @Override
    public Class getViewHolderClassAtPosition(int position) {
        return PhotoListViewHolder.class;
    }

    @Override
    public int getViewHolderCount() {
        return 1;
    }


}
