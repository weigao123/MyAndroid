package com.garfield.weishu.session.sessionlist;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.garfield.weishu.R;
import com.garfield.weishu.base.recyclerview.BaseRecyclerViewHolder;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.garfield.weishu.session.session.emoji.MoonUtil;
import com.garfield.weishu.ui.view.HeadImageView;
import com.garfield.baselib.utils.system.TimeUtil;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.Locale;

import static com.garfield.weishu.session.sessionlist.SessionListFragment.RECENT_TAG_STICKY;

/**
 * Created by gaowei3 on 2016/10/27.
 */

public class SessionListViewHolder extends BaseRecyclerViewHolder<RecentContact> {

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

    }

    @Override
    public void refresh(RecentContact recent) {
        mRecentContact = recent;

//        TypedValue typedValue = new TypedValue();
//        mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
//        mRootView.setBackgroundResource(typedValue.resourceId);
        if ((mRecentContact.getTag() & RECENT_TAG_STICKY) == 0) {
            mRootView.setBackgroundResource(R.drawable.bg_press_item_to);
        } else {
            mRootView.setBackgroundResource(R.drawable.bg_session_sticky);
        }

        mHeadImageView.loadBuddyAvatar(recent.getContactId());
        mNameTextView.setText(UserInfoCache.getInstance().getUserDisplayName(recent.getContactId()));

        MoonUtil.identifyRecentVHFaceExpressionAndTags(mRootView.getContext(), mContentTextView, recent.getContent(), -1, 0.45f);
        //mContentTextView.setText(recent.getContent());

        mTime.setText(TimeUtil.getTimeShowString(recent.getTime(), true));
        if (recent.getUnreadCount() > 0) {
            mUnReadNum.setText(String.format(Locale.getDefault(), "%d", recent.getUnreadCount()));
            mUnReadNum.setVisibility(View.VISIBLE);
        } else {
            mUnReadNum.setVisibility(View.GONE);
        }
        switch (recent.getMsgStatus()) {
            case fail:
                mMsgState.setImageResource(R.drawable.ic_failed);
                mMsgState.setVisibility(View.VISIBLE);
                break;
            case sending:
                mMsgState.setImageResource(R.drawable.ic_sending);
                mMsgState.setVisibility(View.VISIBLE);
                break;
            default:
                mMsgState.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void refresh() {
        refresh(mRecentContact);
    }

    @Override
    protected SessionListAdapter getAdapter() {
        return (SessionListAdapter) mAdapter;
    }
}
