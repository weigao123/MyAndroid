package com.garfield.weishu.session.session;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.garfield.baselib.utils.array.StringUtils;
import com.garfield.baselib.utils.system.KeyboardUtils;
import com.garfield.weishu.R;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.session.session.emoji.EmoticonPickerView;
import com.garfield.weishu.session.session.emoji.IEmoticonSelectedListener;
import com.garfield.weishu.session.session.emoji.MoonUtil;
import com.garfield.weishu.session.session.function.FunctionPickerView;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaowei3 on 2016/9/26.
 */

class InputPanel implements IEmoticonSelectedListener, KeyboardLinearLayout.OnMeasureListener {

    @BindView(R.id.message_input_text)
    EditText mInputEtx;

    @BindView(R.id.message_input_send)
    TextView mSendBtn;

    @BindView(R.id.message_input_edit_line)
    View mEditTextLine;

    @BindView(R.id.session_emoticon_picker_view)
    EmoticonPickerView mEmoticonPickerView;

    @BindView(R.id.session_function_picker_view)
    FunctionPickerView mFunctionPickerView;

    @BindView(R.id.message_input_plus)
    ImageView mPlusBtn;

    private View mRootView;
    private String mAccount;
    private ModuleProxy mModuleProxy;
    private Activity mActivity;
    private State mState = State.NONE;

    private enum State {
        NONE,
        KEYBOARD,
        EMOTION,
        FUNCTION
    }

    InputPanel(View rootView, String account, ModuleProxy moduleProxy) {
        mRootView = rootView;
        mAccount = account;
        mModuleProxy = moduleProxy;
        mActivity = ((Fragment) moduleProxy).getActivity();
        ButterKnife.bind(this, rootView);
        init();
    }

    private void init() {
        mInputEtx.requestFocus();

        mInputEtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mState = State.KEYBOARD;
            }
        });

        mInputEtx.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mState = State.KEYBOARD;
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
                checkSendButtonEnable(mInputEtx);
                MoonUtil.replaceEmoticons(AppCache.getContext(), s, start, count);
                int editEnd = mInputEtx.getSelectionEnd();
                mInputEtx.removeTextChangedListener(this);
                while (StringUtils.counterChars(s.toString()) > 5000 && editEnd > 0) {
                    s.delete(editEnd - 1, editEnd);
                    editEnd--;
                }
                mInputEtx.setSelection(editEnd);
                mInputEtx.addTextChangedListener(this);
            }
        });
    }

    /**
     * contentView的尺寸
     * 只要尺寸改变就会调用，包括点击返回键和输入法的隐藏键
     */
    @Override
    public void onMeasure(int nowHeight) {
        // 点击返回键或者输入法的隐藏键触发toggle，只能在这里修改状态
        if (mState == State.KEYBOARD && !KeyboardUtils.isKeyboardShowing(mRootView)) {
            mState = State.NONE;
        }
        switch (mState) {
            case NONE:
                setInputLine(false);
                break;
            case KEYBOARD:
                hideEmotionPanel();
                hideFunctionPanel();
                setInputLine(true);
                break;
            case EMOTION:
                showEmotionPanel();
                setInputLine(true);
                break;
            case FUNCTION:
                showFunctionPanel();
                setInputLine(false);
                break;
        }
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

    @OnClick({R.id.message_input_smile, R.id.message_input_plus})
    void togglePanel(View view) {
        switch (view.getId()) {
            case R.id.message_input_smile:
                if (mEmoticonPickerView.getVisibility() == View.GONE) {
                    toggleToEmotion();
                } else {
                    toggleToKeyboard();
                }
                break;
            case R.id.message_input_plus:
                if (mFunctionPickerView.getVisibility() == View.GONE) {
                    toggleToFunction();
                } else {
                    toggleToKeyboard();
                }
                break;
        }
    }

    private void toggleToEmotion() {
        hideFunctionPanel();
        if (KeyboardUtils.isKeyboardShowing(mRootView)) {
            KeyboardUtils.hideKeyboard(mActivity.getCurrentFocus());
        } else {
            // onMeasure不会回调
            showEmotionPanel();
        }
        mState = State.EMOTION;
    }

    private void toggleToKeyboard() {
        KeyboardUtils.showKeyboard(mInputEtx);
        mState = State.KEYBOARD;
    }

    private void toggleToFunction() {
        hideEmotionPanel();
        if (KeyboardUtils.isKeyboardShowing(mRootView)) {
            KeyboardUtils.hideKeyboard(mActivity.getCurrentFocus());
        } else {
            // onMeasure不会回调
            showFunctionPanel();
        }
        mState = State.FUNCTION;
    }

    private void showEmotionPanel() {
        mEmoticonPickerView.show(InputPanel.this);
        mEmoticonPickerView.setVisibility(View.VISIBLE);
    }

    private void hideEmotionPanel() {
        mEmoticonPickerView.setVisibility(View.GONE);
    }

    private void showFunctionPanel() {
        mFunctionPickerView.setVisibility(View.VISIBLE);
    }

    private void hideFunctionPanel() {
        mFunctionPickerView.setVisibility(View.GONE);
    }

    private void setInputLine(boolean focus) {
        if (focus) {
            mEditTextLine.setBackgroundColor(mRootView.getContext().getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mEditTextLine.setBackgroundColor(mRootView.getContext().getResources().getColor(R.color.gray));
        }
    }

    boolean collapse() {
        if (mState == State.NONE) {
            return false;
        } else {
            mState = State.NONE;
            KeyboardUtils.hideKeyboard(mInputEtx);
            hideEmotionPanel();
            hideFunctionPanel();
            return true;
        }
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

    private void checkSendButtonEnable(EditText editText) {
        String textMessage = editText.getText().toString();
        if (!TextUtils.isEmpty(StringUtils.removeBlanks(textMessage)) && editText.hasFocus()) {
            mPlusBtn.setVisibility(View.GONE);
            mSendBtn.setVisibility(View.VISIBLE);
        } else {
            mSendBtn.setVisibility(View.GONE);
            mPlusBtn.setVisibility(View.VISIBLE);
        }
    }


}
