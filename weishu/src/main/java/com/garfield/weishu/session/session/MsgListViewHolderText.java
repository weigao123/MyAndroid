package com.garfield.weishu.session.session;

import android.graphics.Color;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.garfield.baselib.utils.system.ScreenUtils;
import com.garfield.weishu.R;
import com.garfield.weishu.session.session.emoji.MoonUtil;

/**
 * Created by gaowei3 on 2016/9/30.
 */

public class MsgListViewHolderText extends MsgListViewHolderBase {

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
        mText.setTextColor(isReceivedMessage() ? Color.BLACK : Color.WHITE);
        mText.setLinkTextColor(isReceivedMessage() ? Color.BLACK : Color.WHITE);
        MoonUtil.identifyFaceExpression(mRootView.getContext(), mText, getDisplayText(), ImageSpan.ALIGN_BOTTOM);
    }

    // 点9图右下横线决定内容区域
    private void layoutDirection() {
        if (isReceivedMessage()) {
            //mText.setBackgroundResource(R.drawable.message_item_bg_left_selector);
            mText.setPadding(ScreenUtils.dp2px(8), ScreenUtils.dp2px(8), ScreenUtils.dp2px(8), ScreenUtils.dp2px(8));
        } else {
            //mText.setBackgroundResource(R.drawable.message_item_bg_right_selector);
            mText.setPadding(ScreenUtils.dp2px(8), ScreenUtils.dp2px(8), ScreenUtils.dp2px(8), ScreenUtils.dp2px(8));
        }
    }

//    @Override
//    protected int leftBackground() {
//        return 0;
//    }
//
//    @Override
//    protected int rightBackground() {
//        return 0;
//    }

    protected String getDisplayText() {
        return mMessage.getContent();
    }
}
