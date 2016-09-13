package com.garfield.weishu.contact;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.garfield.weishu.contact.item.AbsContactItem;
import com.garfield.weishu.contact.viewholder.AbsContactViewHolder;
import com.garfield.weishu.contact.viewholder.ContactHolder;
import com.garfield.weishu.contact.viewholder.LabelHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaowei3 on 2016/9/8.
 */
public class ContactDataAdapter extends BaseAdapter {

    private final Map<Integer, Class<? extends AbsContactViewHolder<? extends AbsContactItem>>> viewHolderMap;
    private final ContactDataProvider dataProvider;
    private List<AbsContactItem> datas;
    private final Context context;

    public ContactDataAdapter(Context context, ContactDataProvider dataProvider) {
        this.context = context;
        this.dataProvider = dataProvider;
        this.viewHolderMap = new HashMap<>(6);

    }

    public void addViewHolder(int itemDataType, Class<? extends AbsContactViewHolder<? extends AbsContactItem>> viewHolder) {
        this.viewHolderMap.put(itemDataType, viewHolder);
    }

    public boolean load(boolean reload) {
        if (!reload && !isEmpty()) {
            return false;
        }
        ContactTask task = new ContactTask();
        task.execute();
        return true;
    }

    private class ContactTask extends AsyncTask<Void, Void, List<AbsContactItem>> {
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected List<AbsContactItem> doInBackground(Void... params) {
            return dataProvider.provide();
        }
        @Override
        protected void onPostExecute(List<AbsContactItem> datas) {
            updateData(datas);
        }
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return datas != null ? datas.get(position) : null;
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
                holder = (AbsContactViewHolder<AbsContactItem>) viewHolderMap.get(item.getItemType()).newInstance();
                if (holder != null) {
                    holder.createView(context);
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
        Integer[] types = viewHolderMap.keySet().toArray(new Integer[viewHolderMap.size()]);

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
        return viewHolderMap.size();
    }



    private void updateData(List<AbsContactItem> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }





}
