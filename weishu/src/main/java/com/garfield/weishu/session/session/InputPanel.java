package com.garfield.weishu.session.session;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.garfield.weishu.R;
import com.netease.nimlib.sdk.msg.MessageBuilder;
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

    @BindView(R.id.message_input_edit_line)
    View mEditTextLine;

    private View mRootView;
    private String mAccount;
    private ModuleProxy mModuleProxy;

    public InputPanel(View rootView, String account, ModuleProxy moduleProxy) {
        mRootView = rootView;
        mAccount = account;
        mModuleProxy = moduleProxy;
        ButterKnife.bind(this, rootView);
        init();
    }

    private void init() {

        initTextEdit();

    }

    private void initTextEdit() {
        mInputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEditTextLine.setBackgroundColor(mRootView.getContext().getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    mEditTextLine.setBackgroundColor(mRootView.getContext().getResources().getColor(R.color.gray));
                }
            }
        });
    }

    @OnClick(R.id.message_input_send)
    void sendMessage() {
//        AppDownloadManager.newInstance(mRootView.getContext()).download("http://download.fir.im/v2/app/install/58049e9cca87a818b3000a79?download_token=9ef3f7fdea801f6159fa816e35d5eaa6",
//                "weishu.apk");
        String text = mInputText.getText().toString();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        IMMessage textMessage = MessageBuilder.createTextMessage(mAccount, SessionTypeEnum.P2P, text);
        mInputText.setText("");
        mModuleProxy.sendMessage(textMessage);
    }

    public boolean collapse(boolean immediately) {
        hideInputMethod();
        return false;
    }

    // 隐藏键盘布局
    private void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) mRootView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInputText.getWindowToken(), 0);
        mInputText.clearFocus();
    }
}
