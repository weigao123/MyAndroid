package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

import com.garfield.weishu.R;

import butterknife.BindView;
import butterknife.OnClick;

import static com.garfield.weishu.AppCache.USER_ACCOUNT;

/**
 * Created by gaowei3 on 2016/8/4.
 */
public class SessionFragment extends AppBaseFragment {

    @BindView(R.id.message_input_text)
    EditText mInputText;

    @BindView(R.id.message_input_send)
    Button mSendBtn;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_session;
    }

    @Override
    protected boolean onEnableSwipe() {
        return true;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);

    }

    public static SessionFragment newInstance(String account) {
        Bundle args = new Bundle();
        args.putString(USER_ACCOUNT, account);
        SessionFragment fragment = new SessionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.message_input_send)
    void sendMessage() {

    }
}
