package com.garfield.weishu.discovery.news.view;

import android.content.Context;

import com.garfield.weishu.base.recyclerview.TRecyclerAdapter;
import com.garfield.weishu.base.recyclerview.TRecyclerViewHolder;
import com.garfield.weishu.discovery.news.bean.netease.NewsBean;
import com.garfield.weishu.discovery.news.bean.zhihu.ZhihuDaily;

import java.util.List;

/**
 * Created by gaowei3 on 2016/12/5.
 */

public class ZhihuListViewHolder extends TRecyclerViewHolder<ZhihuDaily> {
    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected void inflateView() {

    }

    @Override
    protected void setView() {

    }

    @Override
    protected void refresh(ZhihuDaily zhihuDaily) {

    }

    @Override
    protected void refresh() {

    }

    @Override
    protected TRecyclerAdapter getAdapter() {
        return null;
    }


    public static class ZhihuRecyclerAdapter extends TRecyclerAdapter<ZhihuDaily> {
        public ZhihuRecyclerAdapter(Context context, List<ZhihuDaily> items) {
            super(context, items);
        }
        @Override
        public Class getViewHolderClassAtPosition(int position) {
            return ZhihuListViewHolder.class;
        }
    }
}
