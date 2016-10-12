package com.garfield.weishu.session.viewholder;

import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.garfield.weishu.R;

import java.util.Map;

/**
 * Created by huangjun on 2015/11/25.
 * Tip类型消息ViewHolder
 */
public class MsgViewHolderTip extends MsgViewHolderBase {

    protected TextView notificationTextView;

    @Override
    protected int getContentResId() {
        return R.layout.item_msg_tip;
    }

    @Override
    protected void inflateContentView() {
        notificationTextView = findView(R.id.msg_item_tip);
    }

    @Override
    protected void bindContentView() {
        String text = "未知通知提醒";
        if(TextUtils.isEmpty(mMessage.getContent())) {
            Map<String, Object> content = mMessage.getRemoteExtension();
            if (content != null && !content.isEmpty()) {
                text = (String) content.get("content");
            }
        } else {
            text = mMessage.getContent();
        }

        handleTextNotification(text);
    }

    private void handleTextNotification(String text) {
        //MoonUtil.identifyFaceExpressionAndATags(context, notificationTextView, text, ImageSpan.ALIGN_BOTTOM);
        //notificationTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }
}
