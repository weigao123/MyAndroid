package com.garfield.weishu.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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

    @BindView(R.id.login_account)
    ClearableEditText mAccountText;

    @BindView(R.id.login_password)
    ClearableEditText mPasswordText;

    private AbortableFuture<LoginInfo> mLoginRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {

    }

    @Override
    protected int getToolbarTitle() {
        return R.string.please_login;
    }

    @OnClick(R.id.login_login)
    void login() {
        DialogMaker.showProgressDialog(this, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mLoginRequest != null) {
                    mLoginRequest.abort();
                    onLoginDone();
                }
            }
        }).setCanceledOnTouchOutside(false);

        final String account = mAccountText.getText().toString().toLowerCase();
        final String password = mPasswordText.getText().toString().toLowerCase();
        mLoginRequest = NimInit.login(account, password, new NimInit.LoginResult() {
            @Override
            public void onResult(int result) {
                if (result == NimInit.LOGIN_SUCCESS) {
                    Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    saveLoginInfo(account, password);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else if (result == NimInit.LOGIN_FAILED_A_P_WRONG) {
                    Toast.makeText(LoginActivity.this, R.string.login_account_or_password_wrong, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                }
                onLoginDone();
            }
        });
    }

    private void saveLoginInfo(final String account, final String token) {
        UserPreferences.saveUserAccount(account);
        UserPreferences.saveUserToken(token);
    }

    private void onLoginDone() {
        mLoginRequest = null;
        DialogMaker.dismissProgressDialog();
    }
}
