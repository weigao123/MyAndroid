package com.garfield.weishu.session;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.garfield.weishu.AppCache;
import com.garfield.weishu.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaowei3 on 2016/9/26.
 */

public class InputPanel {

    @BindView(R.id.message_input_text)
    EditText mInputText;

    @BindView(R.id.message_input_send)
    TextView mSendBtn;

    private View mRootView;
    private String mAccount;
    private ModuleProxy mModuleProxy;

    public InputPanel(View rootView, String account, ModuleProxy moduleProxy) {
        mRootView = rootView;
        mAccount = account;
        mModuleProxy = moduleProxy;
        ButterKnife.bind(this, rootView);
    }

    @OnClick(R.id.message_input_send)
    void sendMessage() {
        String text = mInputText.getText().toString();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        IMMessage textMessage = MessageBuilder.createTextMessage(mAccount, SessionTypeEnum.P2P, text);
        mInputText.setText("");
        mModuleProxy.sendMessage(textMessage);
    }
}
