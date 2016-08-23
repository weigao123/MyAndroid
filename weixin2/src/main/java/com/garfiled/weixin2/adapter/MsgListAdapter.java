package com.garfiled.weixin2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.garfield.baselib.utils.L;
import com.garfiled.weixin2.R;
import com.garfiled.weixin2.bean.MsgListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gwball on 2016/8/3.
 */
public class MsgListAdapter extends RecyclerView.Adapter<MsgListAdapter.MyViewHolder> {

    private List<MsgListBean> mData;
    private OnItemClickListener mClickListener;

    public MsgListAdapter(ArrayList<MsgListBean> data) {
        mData = data;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mNameTextView;
        public final TextView mContentTextView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.item_msglist_head);
            mNameTextView = (TextView) view.findViewById(R.id.item_msglist_name);
            mContentTextView = (TextView) view.findViewById(R.id.item_msglist_content);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msglist, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (mClickListener != null) {
                    mClickListener.onItemClick(position, v);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
