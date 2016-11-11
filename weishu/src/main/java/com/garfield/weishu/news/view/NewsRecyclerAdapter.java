package com.garfield.weishu.news.view;

import android.content.Context;

import com.garfield.weishu.base.recyclerview.TRecyclerAdapter;
import com.garfield.weishu.news.bean.NewsBean;

import java.util.List;

/**
 * Created by gaowei3 on 2016/11/4.
 */

public class NewsRecyclerAdapter extends TRecyclerAdapter<NewsBean> {

    public NewsRecyclerAdapter(Context context, List<NewsBean> items) {
        super(context, items);
    }

    @Override
    public Class getViewHolderClassAtPosition(int position) {
        return NewsViewHolder.class;
    }


    public void refreshItems(List<NewsBean> items) {
        getItems().clear();
        getItems().addAll(items);
        notifyDataSetChanged();
    }

}
