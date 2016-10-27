package com.garfield.weishu.setting;

import android.content.Context;

import com.garfield.weishu.base.listview.TListAdapter;
import com.garfield.weishu.base.listview.TListAdapterDelegate;

import java.util.List;

/**
 * Created by gaowei3 on 2016/10/19.
 */

public class PhotoListAdapter extends TListAdapter<String> {


    public PhotoListAdapter(Context context, List<String> items, TListAdapterDelegate delegate) {
        super(context, items, delegate);
    }




}
