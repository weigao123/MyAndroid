package com.garfield.weishu.session.session;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.garfield.baselib.utils.system.ScreenUtil;
import com.garfield.weishu.R;
import com.garfield.weishu.base.listview.TListViewHolder;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.ui.view.HeadImageView;
import com.garfield.baselib.utils.system.TimeUtil;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by gwball on 2016/9/27.
 */

public abstract class MsgListViewHolderBase extends TListViewHolder<IMMessage> {
    protected IMMessage mMessage;

    protected RelativeLayout mHolderBase;
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

    {
        mRegisterRootListener = false;
    }

    @Override
    protected int getResId() {
        return R.layout.item_msg_base;
    }

    @Override
    protected void inflateView() {
        mHolderBase = findView(R.id.msg_item_base);
        mTimeText = findView(R.id.msg_item_time);
        mLeftHead = findView(R.id.msg_item_left_head);
        mRightHead = findView(R.id.msg_item_right_head);
        mBodyContainer = findView(R.id.msg_item_body);
        mProgressBar = findView(R.id.msg_item_progress);
        mAlert = findView(R.id.msg_item_alert);
        mAlreadyRead = findView(R.id.msg_item_already_read);
        mContentContainer = findView(R.id.msg_item_content);

        View.inflate(mRootView.getContext(), getContentResId(), mContentContainer);
        inflateContentView();
    }

    @Override
    public void setView() {
        // 重发/重收按钮响应事件
        if (getAdapter().getEventListener() != null) {
            mAlert.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getAdapter().getEventListener().onFailedBtnClick(mMessage);
                }
            });
        }

        mContentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick();
            }
        });

        // 头像点击事件响应
        View.OnClickListener portraitListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    @Override
    protected void refresh(IMMessage item) {
        mMessage = item;
        setHeadImageView();
        setTimeTextView();
        setSentStatus();
        setContent();
        setReadReceipt();

        bindContentView();
    }

    public void refreshCurrentItem() {
        if (mMessage != null) {
            refresh(mMessage);
        }
    }

    // 内容区域点击事件响应处理。
    protected void onItemClick() {
    }

    private void setHeadImageView() {
        if (isReceivedMessage()) {
            mLeftHead.setVisibility(View.VISIBLE);
            mRightHead.setVisibility(View.GONE);
            mLeftHead.loadBuddyAvatar(mMessage.getFromAccount());
        } else {
            mLeftHead.setVisibility(View.GONE);
            mRightHead.setVisibility(View.VISIBLE);
            mRightHead.loadBuddyAvatar(mMessage.getFromAccount());
        }
    }

    /**
     * 设置时间显示
     */
    private void setTimeTextView() {
        MsgListAdapter msgAdapter = getAdapter();
        if (msgAdapter.needShowTime(mMessage)) {
            mTimeText.setVisibility(View.VISIBLE);
        } else {
            mTimeText.setVisibility(View.GONE);
            return;
        }

        String text = TimeUtil.getTimeShowString(mMessage.getTime(), false);
        mTimeText.setText(text);
    }

    private void setSentStatus() {
        MsgStatusEnum status = mMessage.getStatus();
        switch (status) {
            case fail:
                mProgressBar.setVisibility(View.GONE);
                mAlert.setVisibility(View.VISIBLE);
                break;
            case sending:
                mProgressBar.setVisibility(View.VISIBLE);
                mAlert.setVisibility(View.GONE);
                break;
            default:
                mProgressBar.setVisibility(View.GONE);
                mAlert.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 调整container的位置，左边还是右边
     */
    private void setContent() {
        int index = isReceivedMessage() ? 0 : 3;
        if (mBodyContainer.getChildAt(index) != mContentContainer) {
            mBodyContainer.removeView(mContentContainer);
            mBodyContainer.addView(mContentContainer, index);
        }

        if (isReceivedMessage()) {
            setGravity(mBodyContainer, Gravity.START);
            mContentContainer.setBackgroundResource(leftBackground());
        } else {
            setGravity(mBodyContainer, Gravity.END);
            // mContentContainer.setBackgroundResource(rightBackground());
            // NinePatchDrawable造成和直接用xml的padding不一样
            mContentContainer.setPadding(ScreenUtil.dp2px(2.5f), ScreenUtil.dp2px(2.5f), ScreenUtil.dp2px(6), ScreenUtil.dp2px(2.5f));
            mContentContainer.setBackgroundDrawable(rightBackground());
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
    protected MsgListAdapter getAdapter() {
        return (MsgListAdapter) mAdapter;
    }

    protected boolean isReceivedMessage() {
        return mMessage.getDirect() == MsgDirectionEnum.In;
    }

    protected final void setGravity(View view, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = gravity;
        if (gravity == Gravity.START) {
            params.leftMargin = 0;
            params.rightMargin = ScreenUtil.dp2px(40);
        } else {
            params.leftMargin = ScreenUtil.dp2px(40);
            params.rightMargin = 0;
        }
    }

    protected int leftBackground() {
        return R.drawable.message_item_bg_left_selector;
    }

//    protected int rightBackground() {
//        return R.drawable.message_item_bg_right_selector;
//    }

    protected Drawable rightBackground() {
        Resources resources = mRootView.getContext().getResources();
        Bitmap bitmapRight = BitmapFactory.decodeResource(resources, R.drawable.message_right_blue_bg);
        Bitmap bitmapRightPressed = BitmapFactory.decodeResource(resources, R.drawable.message_right_blue_bg_pressed);
        //BitmapDrawable drawableRight = new BitmapDrawable(resources, bitmapRight);
        //BitmapDrawable drawableRightPressed = new BitmapDrawable(resources, bitmapRightPressed);

        NinePatchDrawable nineDrawableRight = new NinePatchDrawable(resources, bitmapRight, bitmapRight.getNinePatchChunk(), new Rect(), null);
        NinePatchDrawable nineDrawableRightPressed = new NinePatchDrawable(resources, bitmapRightPressed, bitmapRightPressed.getNinePatchChunk(), new Rect(), null);
        nineDrawableRight.setColorFilter(new PorterDuffColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP));
        nineDrawableRightPressed.setColorFilter(new PorterDuffColorFilter(resources.getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP));

        StateListDrawable sld = new StateListDrawable();
        sld.addState(new int[]{android.R.attr.state_pressed}, nineDrawableRightPressed);
        sld.addState(new int[]{}, nineDrawableRight);
        return sld;
    }

    protected boolean isMiddleItem() {
        return false;
    }

}
