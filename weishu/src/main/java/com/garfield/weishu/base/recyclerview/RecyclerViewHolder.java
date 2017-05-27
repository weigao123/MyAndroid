package com.garfield.weishu.base.recyclerview;

/**
 * Created by gaowei3 on 2016/10/29.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * position -> type -> BaseRecyclerViewHolder -> 根据TRecyclerViewHolder里的res创建View ->
 * 注入到RecyclerViewHolder -> 把View绑定到TRecyclerViewHolder上
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private BaseRecyclerViewHolder mTViewHolder;

    RecyclerViewHolder(View view) {
        super(view);
    }

    RecyclerViewHolder(LayoutInflater inflater, ViewGroup parent, BaseRecyclerAdapter adapter, BaseRecyclerViewHolder item) {
        super(inflater.inflate(item.getLayoutResId(), parent, false));
        this.mTViewHolder = item;
        this.mTViewHolder.bindViews(itemView, adapter);
    }

    BaseRecyclerViewHolder getTViewHolder() {
        return mTViewHolder;
    }

    /**
     * 加载完成之后，用于在外部调用，使个别item刷新数据
     */
    public void refresh() {
        mTViewHolder.refresh();
    }
}