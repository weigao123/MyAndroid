package com.garfield.weishu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.garfield.baselib.widget.ClearableEditText;
import com.garfield.weishu.R;
import com.garfield.weishu.base.AppBaseActivity;
import com.garfield.weishu.config.Preferences;
import com.garfield.weishu.sdk.nim.NimUtils;

/**
 * Created by gaowei3 on 2016/8/30.
 */
public class LoginActivity extends AppBaseActivity {

    private ClearableEditText mAccountText;
    private ClearableEditText mPasswordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mAccountText = (ClearableEditText) findViewById(R.id.login_account);
        mPasswordText = (ClearableEditText) findViewById(R.id.login_password);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_login:
                login();
                break;
        }
    }

    private void login() {
        final String account = mAccountText.getText().toString().toLowerCase();
        final String password = mPasswordText.getText().toString().toLowerCase();
        NimUtils.login(account, password, new NimUtils.LoginResult() {
                    @Override
                    public void onResult(int result) {
                        if (result == NimUtils.LOGIN_SUCCESS) {
                            Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                            saveLoginInfo(account, password);
                            finish();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else if (result == NimUtils.LOGIN_FAILED_A_P_WRONG) {
                            Toast.makeText(LoginActivity.this, R.string.login_account_or_password_wrong, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }


}
