package com.garfiled.weixin2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.garfiled.weixin2.R;
import com.garfiled.weixin2.bean.ContactBean;
import com.garfiled.weixin2.bean.MsgListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gwball on 2016/8/3.
 */
public class MsgListAdapter extends RecyclerView.Adapter<MsgListAdapter.ViewHolder> {

    List<MsgListBean> mData;

    public MsgListAdapter(ArrayList<MsgListBean> data) {
        mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mNameTextView;
        public final TextView mContentTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.item_msglist_head);
            mNameTextView = (TextView) view.findViewById(R.id.item_msglist_name);
            mContentTextView = (TextView) view.findViewById(R.id.item_msglist_content);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msglist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mImageView.setImageResource(mData.get(position).getHeadImage());
        holder.mNameTextView.setText(mData.get(position).getName());
        holder.mContentTextView.setText(mData.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(ArrayList<MsgListBean> data) {
        mData = data;
    }
}
