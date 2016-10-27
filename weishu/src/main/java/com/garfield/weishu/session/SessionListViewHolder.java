package com.garfield.weishu.session;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.garfield.weishu.R;
import com.garfield.weishu.base.adapter.OnItemClickListener;
import com.garfield.weishu.base.adapter.OnItemLongClickListener;
import com.garfield.weishu.base.recyclerview.TRecyclerViewHolder;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.garfield.weishu.ui.view.HeadImageView;
import com.garfield.weishu.utils.TimeUtil;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.Locale;

/**
 * Created by gaowei3 on 2016/10/27.
 */

public class SessionListViewHolder extends TRecyclerViewHolder<RecentContact> {

    public HeadImageView mHeadImageView;
    public TextView mNameTextView;
    public TextView mContentTextView;
    public TextView mTime;
    public TextView mUnReadNum;
    public ImageView mMsgState;
    public String mAccount;

    private OnItemClickListener mClickListener;
    private OnItemLongClickListener mLongClickListener;


    @Override
    public int getLayoutResId() {
        return R.layout.item_msglist;
    }

    @Override
    protected void inflateChildView() {
        mRootView.setBackgroundResource(R.drawable.bg_press_gray);
//        TypedValue typedValue = new TypedValue();
//        mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
//        mView.setBackgroundResource(typedValue.resourceId);


        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (mClickListener != null) {
                    mClickListener.onItemClick(position, v, holder.mAccount);
                }
            }
        });
        mRootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                if (mLongClickListener != null) {
                    mLongClickListener.onItemLongPressed(position, v);
                }
                return true;
            }
        });


        mHeadImageView = findView(R.id.item_msglist_head);
        mNameTextView = findView(R.id.item_msglist_name);
        mContentTextView = findView(R.id.item_msglist_content);
        mTime = findView(R.id.item_msglist_time);
        mUnReadNum = findView(R.id.item_msglist_unread_number_tip);
        mMsgState = findView(R.id.item_msglist_state);
    }

    @Override
    public void refresh(RecentContact recentContact, int position) {
        mHeadImageView.loadBuddyAvatar(recentContact.getContactId());
        mNameTextView.setText(UserInfoCache.getInstance().getUserDisplayName(recentContact.getContactId()));
        mContentTextView.setText(recentContact.getContent());
        mAccount = recentContact.getContactId();
        mTime.setText(TimeUtil.getTimeShowString(recentContact.getTime(), true));
        if (recentContact.getUnreadCount() > 0) {
            mUnReadNum.setText(String.format(Locale.getDefault(), "%d", recentContact.getUnreadCount()));
            mUnReadNum.setVisibility(View.VISIBLE);
        } else {
            mUnReadNum.setVisibility(View.GONE);
        }
    }


    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public void setOnLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.mLongClickListener = itemLongClickListener;
    }
}
