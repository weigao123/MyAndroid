package com.garfield.weishu.news.old;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.garfield.weishu.R;
import com.garfield.weishu.bean.ContactBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gwball on 2016/8/3.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    List<ContactBean> mData;

    public NewsAdapter(ArrayList<ContactBean> data) {
        mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.item_contact_image);
            mTextView = (TextView) view.findViewById(R.id.item_contact_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_old, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mData.get(position).getName());
        holder.mImageView.setImageResource(mData.get(position).getHeadImage());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(ArrayList<ContactBean> data) {
        mData = data;
    }
}
