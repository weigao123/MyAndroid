package com.garfield.weishu.base.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.baselib.utils.L;
import com.garfield.weishu.base.recyclerview.TRecyclerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TPagerAdapter<T> extends PagerAdapter {
    private final Context mContext;
    private final List<T> mItems;
    private final RecycleBin mRecycleBin;
    private final Map<Class<?>, Integer> mViewTypes;
    private final LayoutInflater mInflater;

    private ViewPager mViewPager;
    private ItemEventListener mItemEventListener;

    public TPagerAdapter(Context context, List<T> items) {
        mContext = context;
        mItems = items;
        mViewTypes = new HashMap<>();
        mInflater = LayoutInflater.from(context);
        mRecycleBin = new RecycleBin(getViewTypeCount());
    }

    public Context getContext() {
        return mContext;
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }


    @Override
    public void notifyDataSetChanged() {
        mRecycleBin.clean();
        super.notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int viewType = getItemViewType(position);
        /**
         * 这样可以获取到以前被回收的View
         */
        View convertView = mRecycleBin.getScrapView(position, viewType);
        if (convertView == null) {
            //L.d("instantiateItem position: " + position + "  null");
            convertView = createViewAtPosition(position);
        }
        TPagerViewHolder holder = (TPagerViewHolder) convertView.getTag();
        holder.refresh(mItems.get(position), position);
        /**
         * 主要是add这个view
         */
        container.addView(convertView);
        return convertView;
    }

    private View createViewAtPosition(int position) {
        TPagerViewHolder holder = null;
        try {
            Class<?> viewHolder = getViewHolderClassAtPosition(position);
            holder = (TPagerViewHolder) viewHolder.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        View view = holder.bindViews(mInflater, this);
        // Holder是已经被findView的集合，把holder放到view里，省去后面再findView
        view.setTag(holder);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        /**
         * 主要是remove掉这个view
         */
        container.removeView(view);
        int viewType = getItemViewType(position);
        mRecycleBin.addScrapView(view, position, viewType);
    }

    @Override
    public final boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    public List<T> getItems() {
        return mItems;
    }

    protected int getItemViewType(int position) {
        Class<?> clazz = getViewHolderClassAtPosition(position);
        if (mViewTypes.containsKey(clazz)) {
            return mViewTypes.get(clazz);
        } else {
            int next = mViewTypes.size();
            mViewTypes.put(clazz, next);
            return next;
        }
    }

    public abstract int getViewTypeCount();

    public abstract Class getViewHolderClassAtPosition(int position);


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
