package com.garfield.weishu.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.garfield.weishu.base.recyclerview.TRecyclerAdapter;

import java.util.List;

/**
 * Created by gaowei3 on 2016/11/4.
 */

public class NewsRecyclerAdapter extends TRecyclerAdapter {

    public NewsRecyclerAdapter(Context context, List items) {
        super(context, items);
    }

    @Override
    public Class getViewHolderClassAtPosition(int position) {
        return NewsViewHolder.class;
    }


}
