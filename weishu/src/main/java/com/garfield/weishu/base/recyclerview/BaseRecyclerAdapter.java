package com.garfield.weishu.base.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by gaowei3 on 2016/10/27.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    private final Context mContext;
    private final List<T> mItems;
    private final Map<Class<?>, Integer> mViewTypes;
    private final LayoutInflater mInflater;

    private int TYPE_HEAD = 1000;
    private int TYPE_FOOT = 1001;
    private View mHeadView;
    private View mFootView;
    private ItemEventListener mItemEventListener;

    public BaseRecyclerAdapter(Context context, List<T> items) {
        mContext = context;
        mItems = items;
        mViewTypes = new HashMap<>();
        mInflater = LayoutInflater.from(context);
    }

    public void setHeadView(View headView) {
        mHeadView = headView;
    }

    public View getHeadView() {
        return mHeadView;
    }

    public void setFootView(View footView) {
        mFootView = footView;
        mFootView.setVisibility(View.GONE);
    }

    public View getFootView() {
        return mFootView;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 第二步，根据type，创建Holder
     */
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            return new RecyclerViewHolder(mHeadView);
        }
        if (viewType == TYPE_FOOT) {
            return new RecyclerViewHolder(mFootView);
        }
        BaseRecyclerViewHolder viewHolder = null;
        Class viewHolderClass = getClassByType(viewType);
        try {
            viewHolder = (BaseRecyclerViewHolder) viewHolderClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RecyclerViewHolder(mInflater, parent, this, viewHolder);
    }

    private Class getClassByType(int viewType) {
        Set<Class<?>> set = mViewTypes.keySet();
        for (Class c : set) {
            if (mViewTypes.get(c) == viewType) {
                return c;
            }
        }
        return null;
    }

    /**
     * 第三步，绑定数据
     */
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (mHeadView != null && position == 0) {
            return;
        }
        if (mFootView != null && position == getItemCount() - 1) {
            return;
        }
        /**
         * 下面的position是item真实的位置
         */
        if (mHeadView != null) --position;
        holder.getTViewHolder().refresh(position);
    }

    public List<T> getItems() {
        return mItems;
    }

    /**
     * 在实际的item基础上再加上头和尾
     */
    @Override
    public int getItemCount() {
        int head = mHeadView == null ? 0 : 1;
        int foot = mFootView == null ? 0 : 1;
        return getDataCount() + head + foot;
    }

    public int getDataCount() {
        return mItems.size();
    }

    /**
     * 第一步，根据position确定type
     */
    @Override
    public int getItemViewType(int position) {
        if (mHeadView != null && position == 0) {
            return TYPE_HEAD;
        }
        if (mFootView != null && position == getItemCount() - 1) {
            return TYPE_FOOT;
        }
        /**
         * 下面的position是item真实的位置
         */
        if (mHeadView != null) --position;
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


    public void setItemEventListener(ItemEventListener eventListener) {
        this.mItemEventListener = eventListener;
    }

    public ItemEventListener getItemEventListener() {
        return mItemEventListener;
    }

    public interface ItemEventListener<T> {
        void onItemClick(T item, int position);
        void onItemLongPressed(T item, int position);
    }

    public void refreshItems(List<T> items) {
        getItems().clear();
        getItems().addAll(items);
        notifyDataSetChanged();
    }

    public void addItems(List<T> items) {
        getItems().addAll(items);
        notifyDataSetChanged();
    }

    public void setFootVisible(boolean isShow) {
        if (mFootView != null) {
            mFootView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    public boolean isFootVisible() {
        return mFootView != null && mFootView.getVisibility() == View.VISIBLE;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEAD
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }
}
