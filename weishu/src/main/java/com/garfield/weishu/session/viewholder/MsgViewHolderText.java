package com.garfield.weishu.session.viewholder;

import android.widget.TextView;

import com.garfield.weishu.R;
import com.garfield.weishu.utils.ScreenUtil;

/**
 * Created by gaowei3 on 2016/9/30.
 */

public class MsgViewHolderText extends MsgViewHolderBase {

    private TextView mText;

    @Override
    protected int getContentResId() {
        return R.layout.item_msg_text;
    }

    @Override
    protected void inflateContentView() {
        mText = findView(R.id.msg_item_text);
    }

    @Override
    protected void bindContentView() {
        layoutDirection();
        mText.setText(getDisplayText());
    }

    private void layoutDirection() {
        if (isReceivedMessage()) {
            mText.setBackgroundResource(R.drawable.message_item_left_selector);
            mText.setPadding(ScreenUtil.dip2px(15), ScreenUtil.dip2px(8), ScreenUtil.dip2px(10), ScreenUtil.dip2px(8));
        } else {
            mText.setBackgroundResource(R.drawable.message_item_right_selector);
            mText.setPadding(ScreenUtil.dip2px(10), ScreenUtil.dip2px(8), ScreenUtil.dip2px(15), ScreenUtil.dip2px(8));
        }
    }

    protected String getDisplayText() {
        return mMessage.getContent();
    }
}
