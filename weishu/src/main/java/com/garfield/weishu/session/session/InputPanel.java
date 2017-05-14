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
import com.garfield.baselib.utils.system.KeyboardUtil;
import com.garfield.baselib.utils.system.ScreenUtil;
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

    @BindView(R.id.message_input_voice)
    ImageView mVoiceBtn;

    @BindView(R.id.message_input_plus)
    ImageView mFunctionBtn;

    @BindView(R.id.message_input_smile)
    ImageView mEmotionBtn;

    @BindView(R.id.message_input_voice_btn)
    TextView mPressToVoice;

    private View mRootView;
    private String mAccount;
    private ModuleProxy mModuleProxy;
    private Activity mActivity;
    private State mState = State.NONE;

    private enum State {
        NONE,
        KEYBOARD,
        EMOTION,
        FUNCTION,
        VOICE
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

    @OnClick({R.id.message_input_smile, R.id.message_input_plus, R.id.message_input_voice})
    void togglePanel(View view) {
        switch (view.getId()) {
            case R.id.message_input_voice:
                if (mPressToVoice.getVisibility() == View.GONE) {
                    toggleToVoice();
                } else {
                    toggleToKeyboard();
                }
                break;
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

    private void toggleToKeyboard() {
        KeyboardUtil.showKeyboard(mActivity.getCurrentFocus());
        refreshBtnUi(mState = State.KEYBOARD);
    }

    private void toggleToVoice() {
        if (ScreenUtil.isPortrait() && KeyboardUtil.isKeyboardShowing(mRootView)) {
            KeyboardUtil.hideKeyboard(mActivity.getCurrentFocus());
        }
        refreshBtnUi(mState = State.VOICE);
    }

    private void toggleToEmotion() {
        if (ScreenUtil.isPortrait() && KeyboardUtil.isKeyboardShowing(mRootView)) {
            // 显示面板要延迟到onMeasure
            KeyboardUtil.hideKeyboard(mActivity.getCurrentFocus());
        } else {
            showEmotionPanel();
        }
        refreshBtnUi(mState = State.EMOTION);
    }

    private void toggleToFunction() {
        if (ScreenUtil.isPortrait() && KeyboardUtil.isKeyboardShowing(mRootView)) {
            // 显示面板要延迟到onMeasure
            KeyboardUtil.hideKeyboard(mActivity.getCurrentFocus());
        } else {
            showFunctionPanel();
        }
        refreshBtnUi(mState = State.FUNCTION);
    }

    /**
     * 父布局的尺寸，只要尺寸改变就会调用，包括点击返回键和输入法的隐藏键
     *
     * 键盘切换后，修改App Window特别是bottom的位置，最后调用requestLayout进行重绘
     * 在父布局onMeasure之前，隐藏/显示合适高度的VIEW
     * http://www.w2bc.com/Article/83935
     */
    @Override
    public void onMeasureBefore(int oldHeight, int newHeight) {
        // 非用户主动点击Btn的特殊情况
        if (newHeight < oldHeight) {
            // 父布局缩小，开启输入法，比如点击文本框
            refreshBtnUi(mState = State.KEYBOARD);
        } else if (newHeight > oldHeight && mState == State.KEYBOARD) {
            // 父布局变大，关闭输入法，比如点击返回键或者输入法的隐藏键，无法控制的只有原来是Keyboard的情况
            // 其他的情况都是通过点击Btn，通过正常流程可控
            refreshBtnUi(mState = State.NONE);
        }
        //L.d("State: " + mState + "  oldHeight: " + oldHeight + "  newHeight: " + newHeight);
        //L.d("isKeyboardShowing: " + KeyboardUtil.isKeyboardShowing(mRootView));
        switch (mState) {
            case NONE:
                break;
            case KEYBOARD:
                hideEmotionPanel();
                hideFunctionPanel();
                break;
            case EMOTION:
                hideFunctionPanel();
                showEmotionPanel();
                break;
            case FUNCTION:
                hideEmotionPanel();
                showFunctionPanel();
                break;
            case VOICE:
                hideFunctionPanel();
                hideEmotionPanel();
                break;
        }
    }

    private void refreshBtnUi(State state) {
        switch (state) {
            case NONE:
            case KEYBOARD:
                mVoiceBtn.setImageResource(R.drawable.message_input_voice_selector);
                mEmotionBtn.setImageResource(R.drawable.message_input_emotion_selector);
                mFunctionBtn.setImageResource(R.drawable.message_input_plus_selector);
                mPressToVoice.setVisibility(View.GONE);
                mEditTextLine.setVisibility(View.VISIBLE);
                mInputEtx.setVisibility(View.VISIBLE);
                setInputLine(true);
                break;
            case EMOTION:
                mVoiceBtn.setImageResource(R.drawable.message_input_voice_selector);
                mEmotionBtn.setImageResource(R.drawable.message_input_keyboard_selector);
                mFunctionBtn.setImageResource(R.drawable.message_input_plus_selector);
                mPressToVoice.setVisibility(View.GONE);
                mEditTextLine.setVisibility(View.VISIBLE);
                mInputEtx.setVisibility(View.VISIBLE);
                setInputLine(true);
                break;
            case FUNCTION:
                mVoiceBtn.setImageResource(R.drawable.message_input_voice_selector);
                mEmotionBtn.setImageResource(R.drawable.message_input_emotion_selector);
                mFunctionBtn.setImageResource(R.drawable.message_input_plus_selector);
                mPressToVoice.setVisibility(View.GONE);
                mEditTextLine.setVisibility(View.VISIBLE);
                mInputEtx.setVisibility(View.VISIBLE);
                setInputLine(false);
                break;
            case VOICE:
                mVoiceBtn.setImageResource(R.drawable.message_input_keyboard_selector);
                mEmotionBtn.setImageResource(R.drawable.message_input_emotion_selector);
                mFunctionBtn.setImageResource(R.drawable.message_input_plus_selector);
                mPressToVoice.setVisibility(View.VISIBLE);
                mEditTextLine.setVisibility(View.GONE);
                mInputEtx.setVisibility(View.GONE);
                setInputLine(false);
                break;
        }
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
            mInputEtx.requestFocus();
            mEditTextLine.setBackgroundColor(mRootView.getContext().getResources().getColor(R.color.colorPrimaryDark));
        } else {
            // 不能clearFocus，会再次导致键盘状态紊乱
            // mInputEtx.clearFocus();
            mEditTextLine.setBackgroundColor(mRootView.getContext().getResources().getColor(R.color.gray));
        }
    }

    boolean collapse() {
        if (mState == State.NONE || mState == State.VOICE) {
            return false;
        } else {
            refreshBtnUi(mState = State.NONE);
            KeyboardUtil.hideKeyboard(mInputEtx);
            hideEmotionPanel();
            hideFunctionPanel();
            return true;
        }
    }

    @Override
    public void onEmotionSelected(String key) {
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
            mFunctionBtn.setVisibility(View.INVISIBLE);
            mSendBtn.setVisibility(View.VISIBLE);
        } else {
            mSendBtn.setVisibility(View.INVISIBLE);
            mFunctionBtn.setVisibility(View.VISIBLE);
        }
    }


}
