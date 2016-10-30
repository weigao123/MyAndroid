package com.garfield.weishu.base.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by gaowei3 on 2016/10/27.
 */

public abstract class TRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    private final Context mContext;
    private final List<T> mItems;
    private final Map<Class<?>, Integer> mViewTypes;
    private final LayoutInflater mInflater;

    public TRecyclerAdapter(Context context, List<T> items) {
        mContext = context;
        mItems = items;
        mViewTypes = new HashMap<>();
        mInflater = LayoutInflater.from(context);
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TRecyclerViewHolder viewHolder = null;
        Class viewHolderClass = getClassByType(viewType);
        try {
            viewHolder = (TRecyclerViewHolder) viewHolderClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RecyclerViewHolder(mInflater, parent, this, viewHolder);
    }

    private Class getClassByType(int type) {
        Set<Class<?>> set = mViewTypes.keySet();
        for (Class c : set) {
            if (mViewTypes.get(c) == type) {
                return c;
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.getTViewHolder().refresh(mItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        Class<?> clazz = getViewHolderClassAtPosition(position);
        if (mViewTypes.containsKey(clazz)) {
            return mViewTypes.get(clazz);
        } else {
            int next = mViewTypes.size();
            mViewTypes.put(clazz, next);
            return next;
        }
    }

    public abstract Class getViewHolderClassAtPosition(int position);

}
