package com.garfield.weishu.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.garfield.baselib.ui.dialog.DialogMaker;
import com.garfield.baselib.ui.widget.ClearableEditText;
import com.garfield.weishu.R;
import com.garfield.weishu.config.UserPreferences;
import com.garfield.weishu.nim.RegisterAndLogin;
import com.garfield.weishu.nim.cache.DataCacheManager;
import com.garfield.weishu.nim.InitSDK;
import com.garfield.weishu.utils.permission.MPermission;
import com.garfield.weishu.utils.permission.annotation.OnMPermissionDenied;
import com.garfield.weishu.utils.permission.annotation.OnMPermissionGranted;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by gaowei3 on 2016/8/30.
 */
public class LoginActivity extends AppBaseActivity implements TextWatcher{

    private final int BASIC_PERMISSION_REQUEST_CODE = 110;

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

    @BindView(R.id.activity_login_login)
    TextView mLoginBtn;
    @BindView(R.id.activity_login_register)
    TextView mRegisterBtn;

    private RegisterAndLogin.CancelableRequest mCancelableRequest;

    @Override
    protected int onGetActivityLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onInitViewAndData(Bundle savedInstanceState) {
        super.onInitViewAndData(savedInstanceState);
        requestBasicPermission();

        mLoginRegisterText.setVisibility(View.VISIBLE);   //这个按钮在toolbar里
        switchLoginAndRegister(true);
        mLoginAccountText.addTextChangedListener(this);
        mLoginPasswordText.addTextChangedListener(this);
        mRegisterAccountText.addTextChangedListener(this);
        mRegisterNickNameText.addTextChangedListener(this);
        mRegisterPasswordText.addTextChangedListener(this);
    }

    private void switchLoginAndRegister(boolean isLogin) {
        mLoginLayout.setVisibility(isLogin? View.VISIBLE: View.GONE);
        mRegisterLayout.setVisibility(!isLogin? View.VISIBLE: View.GONE);
        mLoginRegisterText.setText(!isLogin? R.string.has_account: R.string.has_no_account);
        mLoginAccountText.setText(UserPreferences.getUserAccount());
        mLoginPasswordText.setText("");
        mRegisterAccountText.setText("");
        mRegisterNickNameText.setText("");
        mRegisterPasswordText.setText("");
        checkBtnState();
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
        mCancelableRequest = RegisterAndLogin.login(account, password, new RegisterAndLogin.RequestResult() {
            @Override
            public void onResult(int result) {
                if (result == RegisterAndLogin.REQUEST_SUCCESS) {
                    saveLoginInfo(account, password);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    DataCacheManager.buildDataCacheAsync();
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
        mCancelableRequest = RegisterAndLogin.register(account, nickname, password, new RegisterAndLogin.RequestResult() {
            @Override
            public void onResult(int result) {
                if (result == RegisterAndLogin.REQUEST_SUCCESS) {
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

    private void checkBtnState() {
        if (!mLoginAccountText.getText().toString().isEmpty() &&
                !mLoginPasswordText.getText().toString().isEmpty()) {
            mLoginBtn.setSelected(false);
            mLoginBtn.setClickable(true);
        } else {
            mLoginBtn.setSelected(true);
            mLoginBtn.setClickable(false);
        }
        if (!mRegisterAccountText.getText().toString().isEmpty() &&
                !mRegisterNickNameText.getText().toString().isEmpty() &&
                !mRegisterPasswordText.getText().toString().isEmpty()) {
            mRegisterBtn.setSelected(false);
            mRegisterBtn.setClickable(true);
        } else {
            mRegisterBtn.setSelected(true);
            mRegisterBtn.setClickable(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        checkBtnState();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void requestBasicPermission() {
        MPermission.with(LoginActivity.this)
                .addRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess(){
        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed(){
        Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
        finish();
    }
}
