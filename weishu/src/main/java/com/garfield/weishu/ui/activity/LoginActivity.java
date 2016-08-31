package com.garfield.weishu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.garfield.baselib.widget.ClearableEditText;
import com.garfield.weishu.R;
import com.garfield.weishu.base.AppBaseActivity;
import com.garfield.weishu.sdk.nim.NimInit;

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
                NimInit.login(mAccountText.getText().toString().toLowerCase(),
                        mPasswordText.getText().toString().toLowerCase(),
                        new NimInit.LoginResult() {
                            @Override
                            public void onResult(int result) {
                                if (result == NimInit.LOGIN_SUCCESS) {
                                    Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                                } else if (result == NimInit.LOGIN_FAILED_A_P_WRONG) {
                                    Toast.makeText(LoginActivity.this, R.string.login_account_or_password_wrong, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
        }
    }


}
