package com.garfield.weishu.session.sessionlist;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.garfield.weishu.R;
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

    private HeadImageView mHeadImageView;
    private TextView mNameTextView;
    private TextView mContentTextView;
    private TextView mTime;
    private TextView mUnReadNum;
    private ImageView mMsgState;
    private RecentContact mRecentContact;

    @Override
    public int getLayoutResId() {
        return R.layout.item_msglist;
    }

    @Override
    protected void inflateView() {
        mHeadImageView = findView(R.id.item_msglist_head);
        mNameTextView = findView(R.id.item_msglist_name);
        mContentTextView = findView(R.id.item_msglist_content);
        mTime = findView(R.id.item_msglist_time);
        mUnReadNum = findView(R.id.item_msglist_unread_number_tip);
        mMsgState = findView(R.id.item_msglist_state);
    }

    @Override
    public void setView() {
        mRootView.setBackgroundResource(R.drawable.bg_press_gray);
//        TypedValue typedValue = new TypedValue();
//        mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
//        mRootView.setBackgroundResource(typedValue.resourceId);
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getAdapter().getEventListener() != null) {
                    getAdapter().getEventListener().onItemClick(mRecentContact);
                }
            }
        });
        mRootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (getAdapter().getEventListener() != null) {
                    getAdapter().getEventListener().onItemLongPressed(mRecentContact);
                }
                return true;
            }
        });
    }

    @Override
    public void refresh(RecentContact recentContact) {
        mRecentContact = recentContact;

        mHeadImageView.loadBuddyAvatar(recentContact.getContactId());
        mNameTextView.setText(UserInfoCache.getInstance().getUserDisplayName(recentContact.getContactId()));
        mContentTextView.setText(recentContact.getContent());
        mTime.setText(TimeUtil.getTimeShowString(recentContact.getTime(), true));
        if (recentContact.getUnreadCount() > 0) {
            mUnReadNum.setText(String.format(Locale.getDefault(), "%d", recentContact.getUnreadCount()));
            mUnReadNum.setVisibility(View.VISIBLE);
        } else {
            mUnReadNum.setVisibility(View.GONE);
        }
    }

    @Override
    protected SessionListAdapter getAdapter() {
        return (SessionListAdapter) mAdapter;
    }
}
