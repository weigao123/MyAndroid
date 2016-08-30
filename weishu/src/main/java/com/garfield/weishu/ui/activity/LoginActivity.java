package com.garfield.weishu.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.garfield.baselib.widget.ClearableEditText;
import com.garfield.weishu.R;
import com.garfield.weishu.base.AppBaseActivity;
import com.garfield.weishu.sdk.nim.NeteaseCloud;

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
                NeteaseCloud.login(mAccountText.getText().toString().toLowerCase(),
                        mPasswordText.getText().toString().toLowerCase());
                break;
        }
    }


}
