package com.garfield.weishu.base.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by gaowei3 on 2016/10/27.
 */

public abstract class TRecyclerAdapter<T> extends RecyclerView.Adapter<TRecyclerAdapter.RecyclerViewHolder> {
    protected final Context mContext;
    private final List<T> mItems;

    public TRecyclerAdapter(Context context, List<T> items) {
        mContext = context;
        mItems = items;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(mContext, parent, createItem(viewType));
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.item.refresh(mItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getItemType(mItems.get(position));
    }

    /**
     * 根据具体位置的元素T值，来决定是什么类型
     */
    public abstract int getItemType(T t);

    /**
     * 根据类型创建Holder
     */
    public abstract TRecyclerViewHolder createItem(int type);


    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        protected TRecyclerViewHolder item;

        RecyclerViewHolder(Context context, ViewGroup parent, TRecyclerViewHolder item) {
            super(LayoutInflater.from(context).inflate(item.getLayoutResId(), parent, false));
            this.item = item;
            this.item.bindViews(itemView);
        }
    }
}
