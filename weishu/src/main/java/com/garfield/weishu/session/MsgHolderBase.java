package com.garfield.weishu.session;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.garfield.weishu.R;
import com.garfield.weishu.base.adapter.TViewHolder;
import com.garfield.weishu.base.HeadImageView;
import com.garfield.weishu.utils.TimeUtil;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by gwball on 2016/9/27.
 */

public class MsgHolderBase extends TViewHolder {
    protected IMMessage mMessage;

    protected TextView mTimeText;
    private HeadImageView mLeftHead;
    private HeadImageView mRightHead;
    protected ProgressBar mProgressBar;
    protected ImageView mAlert;
    protected TextView mAlreadyRead;

    private MsgAdapter mMsgAdapter;

    @Override
    protected int getResId() {
        return R.layout.item_msg;
    }

    @Override
    protected void inflate() {
        mTimeText = findView(R.id.msg_item_time);
        mLeftHead = findView(R.id.msg_item_left_head);
        mRightHead = findView(R.id.msg_item_right_head);
        mProgressBar = findView(R.id.msg_item_progress);
        mAlert = findView(R.id.msg_item_alert);
        mAlreadyRead = findView(R.id.msg_item_already_read);
    }

    @Override
    protected void refresh(Object item) {
        mMessage = (IMMessage) item;
        setHeadImageView();


    }

    private void setHeadImageView() {
        if (isReceivedMessage()) {
            mLeftHead.setVisibility(View.VISIBLE);
            mRightHead.setVisibility(View.GONE);
        } else {
            mLeftHead.setVisibility(View.GONE);
            mRightHead.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置时间显示
     */
    private void setTimeTextView() {
        if (getAdapter().needShowTime(mMessage)) {
            mTimeText.setVisibility(View.VISIBLE);
        } else {
            mTimeText.setVisibility(View.GONE);
            return;
        }

        String text = TimeUtil.getTimeShowString(mMessage.getTime(), false);
        mTimeText.setText(text);
    }

    @Override
    protected MsgAdapter getAdapter() {
        return mMsgAdapter;
    }

    protected boolean isReceivedMessage() {
        return mMessage.getDirect() == MsgDirectionEnum.In;
    }


}
