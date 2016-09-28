package com.garfield.weishu.base;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public List<T> getItems() {
        return mItems;
    }

}
