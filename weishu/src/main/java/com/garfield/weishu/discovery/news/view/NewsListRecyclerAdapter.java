package com.garfield.weishu.discovery.news.view;

import android.content.Context;

import com.garfield.weishu.base.recyclerview.TRecyclerAdapter;
import com.garfield.weishu.discovery.news.bean.netease.NewsBean;

import java.util.List;

/**
 * Created by gaowei3 on 2016/11/4.
 */

public class NewsListRecyclerAdapter extends TRecyclerAdapter<NewsBean> {

    public NewsListRecyclerAdapter(Context context, List<NewsBean> items) {
        super(context, items);
    }

    @Override
    public Class getViewHolderClassAtPosition(int position) {
        return NewsListViewHolder.class;
    }

}
