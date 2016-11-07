package com.garfield.weishu.base.listview;

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

public abstract class TListAdapter<T> extends BaseAdapter {
    private final Context mContext;
    private final List<T> mItems;
    private final Map<Class<?>, Integer> mViewTypes;
    private final LayoutInflater mInflater;
    private ItemEventListener mItemEventListener;

    public TListAdapter(Context context, List<T> items) {
        mContext = context;
        mItems = items;
        mInflater = LayoutInflater.from(context);
        // 就算有参数，size()时也只是返回使用的数量
        mViewTypes = new HashMap<Class<?>, Integer>(getViewTypeCount());
    }

    public List<T> getItems() {
        return mItems;
    }

    public Context getContext() {
        return mContext;
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

    /**
     * 会自动根据position确认type，然后自动去寻找这个type的缓存View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent, true);
    }

    // 先根据getItemViewType方法，然后在缓存里查找，找到后给convertView
    public View getView(final int position, View convertView, ViewGroup parent, boolean needRefresh) {
        if (convertView == null) {
            convertView = createViewAtPosition(position);
        }
        TListViewHolder holder = (TListViewHolder) convertView.getTag();
        if (needRefresh) {
            // 对该TAG里取出的holder进行刷新，就是直接对这个View刷新了
            holder.refresh(getItem(position), position);
        }

//        if (holder instanceof IScrollStateListener) {
//            listeners.add(holder);
//        }

        return convertView;
    }

    public View createViewAtPosition(int position) {
        TListViewHolder holder = null;
        View view = null;
        try {
            Class<?> viewHolder = getViewHolderClassAtPosition(position);
            holder = (TListViewHolder) viewHolder.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        view = holder.bindViews(mInflater, this);
        // Holder是已经被findView的集合，把holder放到view里，省去后面再findView
        view.setTag(holder);
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return getViewHolderCount();
    }

    @Override
    public int getItemViewType(int position) {
        // 从0开始
        if (getViewTypeCount() == 1) {
            return 0;
        }

        Class<?> clazz = getViewHolderClassAtPosition(position);
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

    public abstract Class getViewHolderClassAtPosition(int position);

    public abstract int getViewHolderCount();

    public void setItemEventListener(ItemEventListener eventListener) {
        this.mItemEventListener = eventListener;
    }

    public ItemEventListener getItemEventListener() {
        return mItemEventListener;
    }

    public interface ItemEventListener<T> {
        void onItemClick(T item);
        void onItemLongPressed(T item);
    }

}
