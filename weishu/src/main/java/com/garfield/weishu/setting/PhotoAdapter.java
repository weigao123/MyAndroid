package com.garfield.weishu.setting;

import android.content.Context;

import com.garfield.weishu.base.adapter.TAdapter;
import com.garfield.weishu.base.adapter.TAdapterDelegate;
import com.garfield.weishu.base.adapter.TViewHolder;

import java.util.List;

/**
 * Created by gaowei3 on 2016/10/19.
 */

public class PhotoAdapter extends TAdapter<String> {


    public PhotoAdapter(Context context, List<String> items, TAdapterDelegate delegate) {
        super(context, items, delegate);
    }




}
