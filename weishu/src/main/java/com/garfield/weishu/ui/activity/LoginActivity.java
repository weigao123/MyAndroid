package com.garfield.weishu.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garfield.baselib.ui.dialog.DialogMaker;
import com.garfield.baselib.ui.widget.ClearableEditText;
import com.garfield.weishu.R;
import com.garfield.weishu.config.UserPreferences;
import com.garfield.weishu.nim.NimInit;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.auth.LoginInfo;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by gaowei3 on 2016/8/30.
 */
public class LoginActivity extends AppBaseActivity {

    @BindView(R.id.activity_login_layout)
    LinearLayout mLoginLayout;
    @BindView(R.id.activity_login_account)
    ClearableEditText mLoginAccountText;
    @BindView(R.id.activity_login_password)
    ClearableEditText mLoginPasswordText;

    @BindView(R.id.activity_register_layout)
    LinearLayout mRegisterLayout;
    @BindView(R.id.activity_register_account)
    ClearableEditText mRegisterAccountText;
    @BindView(R.id.activity_register_nickname)
    ClearableEditText mRegisterNickNameText;
    @BindView(R.id.activity_register_password)
    ClearableEditText mRegisterPasswordText;

    @BindView(R.id.login_register)
    TextView mLoginRegisterText;

    private NimInit.CancelableRequest mCancelableRequest;

    @Override
    protected int onGetActivityLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onInitViewAndData(Bundle savedInstanceState) {
        super.onInitViewAndData(savedInstanceState);
        mLoginRegisterText.setVisibility(View.VISIBLE);
        switchLoginAndRegister(true);
    }

    private void switchLoginAndRegister(boolean isLogin) {
        mLoginLayout.setVisibility(isLogin? View.VISIBLE: View.GONE);
        mRegisterLayout.setVisibility(!isLogin? View.VISIBLE: View.GONE);
        mLoginRegisterText.setText(!isLogin? R.string.has_account: R.string.has_no_account);
        mLoginAccountText.setText(UserPreferences.getUserAccount());
        mRegisterAccountText.setText(UserPreferences.getUserAccount());
    }

    @OnClick(R.id.login_register)
    void switchLoginAndRegister(TextView view) {
        if (view.getText().equals(getString(R.string.has_account))) {
            switchLoginAndRegister(true);
        } else {
            switchLoginAndRegister(false);
        }
    }

    @OnClick(R.id.activity_login_login)
    void login() {
        DialogMaker.showProgressDialog(this, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mCancelableRequest != null) {
                    mCancelableRequest.cancel();
                    onRequestDone();
                }
            }
        }).setCanceledOnTouchOutside(false);

        final String account = mLoginAccountText.getText().toString().toLowerCase();
        final String password = mLoginPasswordText.getText().toString().toLowerCase();
        mCancelableRequest = NimInit.login(account, password, new NimInit.RequestResult() {
            @Override
            public void onResult(int result) {
                if (result == NimInit.REQUEST_SUCCESS) {
                    saveLoginInfo(account, password);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                onRequestDone();
            }
        });
    }

    @OnClick(R.id.activity_login_register)
    void register() {
        DialogMaker.showProgressDialog(this, getString(R.string.registering), true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mCancelableRequest != null) {
                    mCancelableRequest.cancel();
                    onRequestDone();
                }
            }
        }).setCanceledOnTouchOutside(false);

        final String account = mRegisterAccountText.getText().toString().toLowerCase();
        final String nickname = mRegisterNickNameText.getText().toString().toLowerCase();
        final String password = mRegisterPasswordText.getText().toString().toLowerCase();
        mCancelableRequest = NimInit.register(account, nickname, password, new NimInit.RequestResult() {
            @Override
            public void onResult(int result) {
                if (result == NimInit.REQUEST_SUCCESS) {
                    saveLoginInfo(account, null);
                    mLoginPasswordText.setText(password);
                    switchLoginAndRegister(true);
                }
                onRequestDone();
            }
        });
    }

    private void saveLoginInfo(String account, String token) {
        UserPreferences.saveUserAccount(account);
        if (token != null) {
            UserPreferences.saveUserToken(token);
        }
    }

    private void onRequestDone() {
        mCancelableRequest = null;
        DialogMaker.dismissProgressDialog();
    }
}
