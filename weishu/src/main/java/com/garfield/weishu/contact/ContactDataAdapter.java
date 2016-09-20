package com.garfield.weishu.contact;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.garfield.weishu.contact.item.AbsContactItem;
import com.garfield.weishu.contact.model.AbsContactDataList;
import com.garfield.weishu.contact.model.ContactDataList;
import com.garfield.weishu.contact.model.ContactGroupStrategy;
import com.garfield.weishu.contact.viewholder.AbsContactViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaowei3 on 2016/9/8.
 */
public class ContactDataAdapter extends BaseAdapter {

    private final Map<Integer, Class<? extends AbsContactViewHolder<? extends AbsContactItem>>> mViewHolderMap;

    private ContactGroupStrategy mGroupStrategy;
    private final ContactDataProvider mDataProvider;
    private AbsContactDataList mDatas;

    private final Context mContext;

    public ContactDataAdapter(Context context, ContactGroupStrategy groupStrategy, ContactDataProvider dataProvider) {
        this.mContext = context;
        this.mGroupStrategy = groupStrategy;
        this.mDataProvider = dataProvider;
        this.mViewHolderMap = new HashMap<>(6);

    }

    public void addViewHolder(int itemDataType, Class<? extends AbsContactViewHolder<? extends AbsContactItem>> viewHolder) {
        this.mViewHolderMap.put(itemDataType, viewHolder);
    }

    // 返回false表示直接返回已有数据，不需要重新加载，true表示异步加载
    public boolean load(boolean reload) {
        if (!reload && !isEmpty()) {
            return false;
        }
        ContactTask task = new ContactTask();
        task.execute();
        return true;
    }

    private class ContactTask extends AsyncTask<Void, Void, AbsContactDataList> {
        private ContactDataList mContactDataList = new ContactDataList(mGroupStrategy);

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected AbsContactDataList doInBackground(Void... params) {
            List<AbsContactItem> items = mDataProvider.provide();
            for (AbsContactItem item : items) {
                mContactDataList.add(item);
            }
            mContactDataList.build();
            return mContactDataList;
        }

        @Override
        protected void onPostExecute(AbsContactDataList datas) {
            updateData(datas);
        }
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.getCount() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mDatas != null ? mDatas.getItem(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AbsContactItem item = (AbsContactItem) getItem(position);
        if (item == null) {
            return null;
        }
        AbsContactViewHolder<AbsContactItem> holder = null;
        try {
            if (convertView == null || (holder = (AbsContactViewHolder<AbsContactItem>) convertView.getTag()) == null) {
                holder = (AbsContactViewHolder<AbsContactItem>) mViewHolderMap.get(item.getItemType()).newInstance();
                if (holder != null) {
                    holder.createView(mContext);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (holder == null) {
            return null;
        }

        holder.refresh(this, position, item);
        convertView = holder.getView();
        if (convertView != null) {
            convertView.setTag(holder);
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = getItem(position);
        if (obj == null) {
            return -1;
        }
        AbsContactItem item = (AbsContactItem) obj;
        int type = item.getItemType();
        Integer[] types = mViewHolderMap.keySet().toArray(new Integer[mViewHolderMap.size()]);

        for (int i = 0; i < types.length; i++) {
            int itemType = types[i];
            if (itemType == type) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getViewTypeCount() {
        return mViewHolderMap.size();
    }



    private void updateData(AbsContactDataList datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }





}
