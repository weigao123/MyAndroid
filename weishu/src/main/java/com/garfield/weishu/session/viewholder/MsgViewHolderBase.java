package com.garfield.weishu.session.viewholder;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.garfield.baselib.utils.L;
import com.garfield.weishu.R;
import com.garfield.weishu.base.adapter.TViewHolder;
import com.garfield.weishu.base.HeadImageView;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.session.MsgAdapter;
import com.garfield.weishu.utils.TimeUtil;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by gwball on 2016/9/27.
 */

public abstract class MsgViewHolderBase extends TViewHolder {
    protected IMMessage mMessage;

    protected MyRelativeLayout mHolderBase;
    protected TextView mTimeText;
    private HeadImageView mLeftHead;
    private HeadImageView mRightHead;
    protected LinearLayout mBodyContainer;
    protected FrameLayout mContentContainer;
    protected ProgressBar mProgressBar;
    protected ImageView mAlert;
    protected TextView mAlreadyRead;

    abstract protected int getContentResId();
    abstract protected void inflateContentView();
    abstract protected void bindContentView();

    @Override
    protected int getResId() {
        return R.layout.item_msg_base;
    }

    @Override
    protected void inflateChildView() {
        mHolderBase = findView(R.id.msg_item_base);
        mTimeText = findView(R.id.msg_item_time);
        mLeftHead = findView(R.id.msg_item_left_head);
        mRightHead = findView(R.id.msg_item_right_head);
        mBodyContainer = findView(R.id.msg_item_body);
        mProgressBar = findView(R.id.msg_item_progress);
        mAlert = findView(R.id.msg_item_alert);
        mAlreadyRead = findView(R.id.msg_item_already_read);
        mContentContainer = findView(R.id.msg_item_content);

        View.inflate(mView.getContext(), getContentResId(), mContentContainer);
        inflateContentView();
    }

    @Override
    protected void refresh(Object item) {
        mMessage = (IMMessage) item;
        setHeadImageView();
        setTimeTextView();
        setStatus();
        setContent();
        setReadReceipt();

        setOnClickListener();

        bindContentView();
    }

    // 内容区域点击事件响应处理。
    protected void onItemClick() {
    }

    private void setOnClickListener() {
        // 重发/重收按钮响应事件
        if (getAdapter().getEventListener() != null) {
            mAlert.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getAdapter().getEventListener().onFailedBtnClick(mMessage);
                }
            });
        }

        mHolderBase.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                L.d("root1");
                return false;
            }
        });
        mHolderBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.d("root2");
            }
        });
        mContentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.d("2");
                onItemClick();
            }
        });

        // 头像点击事件响应
        View.OnClickListener portraitListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.d("3");
                EventDispatcher.getFragmentJumpEvent().onShowUserProfile(mMessage.getFromAccount());
            }
        };
        mLeftHead.setOnClickListener(portraitListener);
        mRightHead.setOnClickListener(portraitListener);


        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (getAdapter().getEventListener() != null) {
                    getAdapter().getEventListener().onViewHolderLongClick(mMessage);
                    return true;
                }
                return false;
            }
        };
        mContentContainer.setOnLongClickListener(longClickListener);
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
        MsgAdapter msgAdapter = getAdapter();
        if (msgAdapter.needShowTime(mMessage)) {
            mTimeText.setVisibility(View.VISIBLE);
        } else {
            mTimeText.setVisibility(View.GONE);
            return;
        }

        String text = TimeUtil.getTimeShowString(mMessage.getTime(), false);
        mTimeText.setText(text);
    }

    private void setStatus() {
        MsgStatusEnum status = mMessage.getStatus();
        switch (status) {
            case fail:
                mProgressBar.setVisibility(View.GONE);
                mAlreadyRead.setVisibility(View.VISIBLE);
                break;
            case sending:
                mProgressBar.setVisibility(View.VISIBLE);
                mAlreadyRead.setVisibility(View.GONE);
                break;
            default:
                mProgressBar.setVisibility(View.GONE);
                mAlreadyRead.setVisibility(View.GONE);
                break;
        }
    }

    private void setContent() {
        // 调整container的位置
        int index = isReceivedMessage() ? 0 : 3;
        if (mBodyContainer.getChildAt(index) != mContentContainer) {
            mBodyContainer.removeView(mContentContainer);
            mBodyContainer.addView(mContentContainer, index);
        }

        if (isReceivedMessage()) {
            setGravity(mBodyContainer, Gravity.LEFT);
            mContentContainer.setBackgroundResource(leftBackground());
        } else {
            setGravity(mBodyContainer, Gravity.RIGHT);
            mContentContainer.setBackgroundResource(rightBackground());
        }
    }

    private void setReadReceipt() {
        if (!TextUtils.isEmpty(getAdapter().getUuid()) && mMessage.getUuid().equals(getAdapter().getUuid())) {
            mAlreadyRead.setVisibility(View.VISIBLE);
        } else {
            mAlreadyRead.setVisibility(View.GONE);
        }
    }

    @Override
    protected MsgAdapter getAdapter() {
        return (MsgAdapter) mAdapter;
    }

    protected boolean isReceivedMessage() {
        return mMessage.getDirect() == MsgDirectionEnum.In;
    }

    protected final void setGravity(View view, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = gravity;
    }

    protected int leftBackground() {
        return R.drawable.message_item_left_selector;
    }

    protected int rightBackground() {
        return R.drawable.message_item_right_selector;
    }

    protected boolean isMiddleItem() {
        return false;
    }

}
