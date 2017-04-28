package com.garfield.weishu.session.session;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.garfield.baselib.utils.array.StringUtils;
import com.garfield.weishu.R;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.session.session.emoji.EmoticonPickerView;
import com.garfield.weishu.session.session.emoji.IEmoticonSelectedListener;
import com.garfield.weishu.session.session.emoji.MoonUtil;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaowei3 on 2016/9/26.
 */

public class InputPanel implements IEmoticonSelectedListener {

    @BindView(R.id.message_input_text)
    EditText mInputEtx;

    @BindView(R.id.message_input_send)
    TextView mSendBtn;

    @BindView(R.id.message_input_edit_line)
    View mEditTextLine;

    @BindView(R.id.emoticon_picker_view)
    EmoticonPickerView mEmoticonPickerView;

    private View mRootView;
    private String mAccount;
    private ModuleProxy mModuleProxy;
    private Handler mHandler;

    public InputPanel(View rootView, String account, ModuleProxy moduleProxy) {
        mRootView = rootView;
        mAccount = account;
        mModuleProxy = moduleProxy;
        ButterKnife.bind(this, rootView);
        init();
    }

    private void init() {
        mHandler = new Handler();

        mInputEtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEmojiLayout();
            }
        });
        mInputEtx.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideEmojiLayout();
                    mEditTextLine.setBackgroundColor(mRootView.getContext().getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    mEditTextLine.setBackgroundColor(mRootView.getContext().getResources().getColor(R.color.gray));
                }
            }
        });

        mInputEtx.addTextChangedListener(new TextWatcher() {
            private int start;
            private int count;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.start = start;
                this.count = count;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //checkSendButtonEnable(messageEditText);
                MoonUtil.replaceEmoticons(AppCache.getContext(), s, start, count);
                int editEnd = mInputEtx.getSelectionEnd();
                //mInputEtx.removeTextChangedListener(this);
                while (StringUtils.counterChars(s.toString()) > 5000 && editEnd > 0) {
                    s.delete(editEnd - 1, editEnd);
                    editEnd--;
                }
                mInputEtx.setSelection(editEnd);
                //mInputEtx.addTextChangedListener(this);
            }
        });
    }

    @OnClick(R.id.message_input_send)
    void sendMessage() {
        String text = mInputEtx.getText().toString();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        IMMessage textMessage = MessageBuilder.createTextMessage(mAccount, SessionTypeEnum.P2P, text);
        mInputEtx.setText("");
        mModuleProxy.sendMessage(textMessage);
    }

    @OnClick(R.id.message_input_smile)
    void toggleEmoji() {
        if (mEmoticonPickerView.getVisibility() == View.GONE) {
            showEmojiLayout();
        } else {
            hideEmojiLayout();
        }
    }

    private void showEmojiLayout() {
        hideInputMethod();
        mInputEtx.clearFocus();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEmoticonPickerView.setVisibility(View.VISIBLE);
                mEmoticonPickerView.show(InputPanel.this);
            }
        }, 100);
    }

    private void hideEmojiLayout() {
        mEmoticonPickerView.setVisibility(View.GONE);
    }

    private void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) mRootView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInputEtx.getWindowToken(), 0);
        mInputEtx.clearFocus();
    }

    public boolean collapse(boolean immediately) {
        hideAllInputLayout();
        return false;
    }

    private void hideAllInputLayout() {
        hideInputMethod();
        hideEmojiLayout();
    }



    @OnClick({R.id.message_input_audio, R.id.message_input_smile})
    void toast() {
        //L.toast(R.string.function_has_not_developed);
    }

    @Override
    public void onEmojiSelected(String key) {
        Editable mEditable = mInputEtx.getText();
        if (key.equals("/DEL")) {
            mInputEtx.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            int start = mInputEtx.getSelectionStart();
            int end = mInputEtx.getSelectionEnd();
            start = (start < 0 ? 0 : start);
            end = (start < 0 ? 0 : end);
            mEditable.replace(start, end, key);
        }
    }

    @Override
    public void onStickerSelected(String categoryName, String stickerName) {

    }
}
