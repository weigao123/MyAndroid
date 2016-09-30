package com.garfield.weishu.session;

import android.widget.TextView;

import com.garfield.weishu.R;

/**
 * Created by gaowei3 on 2016/9/30.
 */

public class MsgHolderText extends MsgHolderBase {

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
        mText.setText(getDisplayText());
    }

    protected String getDisplayText() {
        return mMessage.getContent();
    }
}
