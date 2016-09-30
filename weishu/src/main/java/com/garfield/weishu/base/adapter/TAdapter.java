package com.garfield.weishu.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaowei3 on 2016/9/28.
 */

public class TAdapter<T> extends BaseAdapter {
    protected final Context mContext;
    private final List<T> mItems;
    private final Map<Class<?>, Integer> mViewTypes;
    private final LayoutInflater mInflater;
    private final TAdapterDelegate mDelegate;

    public TAdapter(Context context, List<T> items, TAdapterDelegate delegate) {
        mContext = context;
        mItems = items;
        mDelegate = delegate;
        mInflater = LayoutInflater.from(context);
        // 就算有参数，size()时也只是返回使用的数量
        mViewTypes = new HashMap<Class<?>, Integer>(getViewTypeCount());
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public T getItem(int position) {
        return position < getCount() ? mItems.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getItems() {
        return mItems;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent, true);
    }

    // 先根据getItemViewType方法，然后在缓存里查找，找到后给convertView
    public View getView(final int position, View convertView, ViewGroup parent, boolean needRefresh) {
        if (convertView == null) {
            convertView = createViewAtPosition(position);
        }
        TViewHolder holder = (TViewHolder) convertView.getTag();
        holder.setPosition(position);
        if (needRefresh) {
            try {
                // 对该TAG里取出的holder进行刷新，就是直接对这个View刷新了
                holder.refresh(getItem(position));
            } catch (RuntimeException e) {
            }
        }

//        if (holder instanceof IScrollStateListener) {
//            listeners.add(holder);
//        }

        return convertView;
    }

    public View createViewAtPosition(int position) {
        TViewHolder holder = null;
        View view = null;
        try {
            Class<?> viewHolder = mDelegate.getViewHolderClassAtPosition(position);
            holder = (TViewHolder) viewHolder.newInstance();
            holder.setAdapter(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        view = holder.getView(mInflater);
        // Holder是已经被findView的集合，把holder放到view里，省去后面再findView
        view.setTag(holder);
        holder.setContext(view.getContext());
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return mDelegate.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        // 从0开始
        if (getViewTypeCount() == 1) {
            return 0;
        }

        Class<?> clazz = mDelegate.getViewHolderClassAtPosition(position);
        if (mViewTypes.containsKey(clazz)) {
            return mViewTypes.get(clazz);
        } else {
            int next = mViewTypes.size();
            if (next < getViewTypeCount()) {
                mViewTypes.put(clazz, next);
                return next;
            }
            return 0;
        }
    }

}
