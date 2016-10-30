package com.garfield.weishu.base.recyclerview;

/**
 * Created by gaowei3 on 2016/10/29.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * position -> type -> TRecyclerViewHolder -> 根据TRecyclerViewHolder里的res创建View ->
 * 注入到RecyclerViewHolder -> 把View绑定到TRecyclerViewHolder上
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private TRecyclerViewHolder mTViewHolder;

    RecyclerViewHolder(LayoutInflater inflater, ViewGroup parent, TRecyclerAdapter adapter, TRecyclerViewHolder item) {
        super(inflater.inflate(item.getLayoutResId(), parent, false));
        this.mTViewHolder = item;
        this.mTViewHolder.bindViews(itemView, adapter);
    }

    TRecyclerViewHolder getTViewHolder() {
        return mTViewHolder;
    }

    /**
     * 加载完成之后，用于个别item刷新
     */
    public void refresh() {
        mTViewHolder.refresh();
    }
}