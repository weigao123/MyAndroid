package com.garfield.weishu.ui.activity;

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
import com.garfield.baselib.utils.system.L;
import com.garfield.baselib.utils.system.SystemUtil;
import com.garfield.weishu.R;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.app.SettingsPreferences;
import com.garfield.weishu.app.UserPreferences;
import com.garfield.weishu.nim.NimHelper;
import com.garfield.weishu.nim.RegisterAndLogin;
import com.garfield.weishu.nim.cache.DataCacheManager;
import com.garfield.weishu.nim.cache.FriendDataCache;
import com.netease.nimlib.sdk.RequestCallbackWrapper;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by gaowei3 on 2016/8/30.
 */
public class LoginActivity extends AppBaseActivity implements TextWatcher {

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
    @BindView(R.id.activity_register_password_repeat)
    ClearableEditText mRegisterPasswordRepeatText;

    @BindView(R.id.login_register)
    TextView mLoginRegisterText;

    @BindView(R.id.activity_login_login)
    TextView mLoginBtn;
    @BindView(R.id.activity_login_register)
    TextView mRegisterBtn;

    private boolean mRegistered;
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
        mRegisterPasswordRepeatText.addTextChangedListener(this);
    }

    private void switchLoginAndRegister(boolean isLogin) {
        mLoginLayout.setVisibility(isLogin? View.VISIBLE: View.GONE);
        mRegisterLayout.setVisibility(!isLogin? View.VISIBLE: View.GONE);
        mLoginRegisterText.setText(!isLogin? R.string.has_account: R.string.has_no_account);
        mLoginAccountText.setText(UserPreferences.getUserAccount());
        mLoginAccountText.setSelection(mLoginAccountText.getText().length());
        mLoginPasswordText.setText(mLoginPasswordText.getTag() == null ? "" : (String)mLoginPasswordText.getTag());
        mRegisterAccountText.setText("");
        mRegisterNickNameText.setText("");
        mRegisterPasswordText.setText("");
        mRegisterPasswordRepeatText.setText("");
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

        String account = mLoginAccountText.getText().toString().trim();
        String password = mLoginPasswordText.getText().toString().trim();
        mCancelableRequest = RegisterAndLogin.login(account, password, new RegisterAndLogin.RequestResult() {
            @Override
            public void onResult(int result) {
                if (result == RegisterAndLogin.REQUEST_SUCCESS) {
                    // 还在转圈
                    if (mRegistered) {
                        addAuthorAndStartMain();
                    } else {
                        startMain();
                    }
                } else {
                    onRequestDone();
                }
            }
        });
    }

    @OnClick(R.id.activity_login_register)
    void register() {
        if (!checkRegisterContentValid()) {
            return;
        }

        final String account = mRegisterAccountText.getText().toString().trim();
        final String nickname = mRegisterNickNameText.getText().toString().trim();
        final String password = mRegisterPasswordText.getText().toString().trim();

        DialogMaker.showProgressDialog(this, getString(R.string.registering), true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mCancelableRequest != null) {
                    mCancelableRequest.cancel();
                    onRequestDone();
                }
            }
        }).setCanceledOnTouchOutside(false);

        mLoginPasswordText.setTag(null);
        mCancelableRequest = RegisterAndLogin.register(account, nickname, password, new RegisterAndLogin.RequestResult() {
            @Override
            public void onResult(int result) {
                onRequestDone();
                if (result == RegisterAndLogin.REQUEST_SUCCESS) {
                    mRegistered = true;
                    saveLoginInfo(account, null);
                    mLoginPasswordText.setTag(password);
                    switchLoginAndRegister(true);
                    login();
                }
            }
        });
    }

    private boolean checkRegisterContentValid() {
        // 帐号检查
        String account = mRegisterAccountText.getText().toString().trim();
        if (account.length() <= 0 || account.length() > 20) {
            L.toast(R.string.account_format_wrong);
            return false;
        }

        // 昵称检查
        String nick = mRegisterNickNameText.getText().toString().trim();
        if (nick.length() <= 0 || nick.length() > 10) {
            L.toast(R.string.nickname_format_wrong);
            return false;
        }

        // 密码检查
        String password = mRegisterPasswordText.getText().toString().trim();
        String passwordRepeat = mRegisterPasswordRepeatText.getText().toString().trim();
        if (password.length() < 6 || password.length() > 20) {
            L.toast(R.string.password_format_wrong);
            return false;
        }
        if (!password.equals(passwordRepeat)) {
            L.toast(R.string.password_repeat_wrong);
            return false;
        }

        return true;
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
            mLoginBtn.setClickable(true);
        } else {
            mLoginBtn.setClickable(false);
        }
        if (!mRegisterAccountText.getText().toString().isEmpty() &&
                !mRegisterNickNameText.getText().toString().isEmpty() &&
                !mRegisterPasswordText.getText().toString().isEmpty() &&
                !mRegisterPasswordRepeatText.getText().toString().isEmpty()) {
            mRegisterBtn.setClickable(true);
        } else {
            mRegisterBtn.setClickable(false);
        }
    }

    private void addAuthorAndStartMain() {
        if (!AppCache.getAccount().equals("gwblue") && !FriendDataCache.getInstance().isMyFriend("gwblue")) {
            NimHelper.addUserAsFriend("gwblue", new RequestCallbackWrapper<Void>() {
                @Override
                public void onResult(int i, Void aVoid, Throwable throwable) {
                    if (i == 200) {
                        startMain();
                    } else {
                        onRequestDone();
                    }
                }
            });
        } else {
            startMain();
        }
    }

    private void startMain() {
        onRequestDone();
        String account = mLoginAccountText.getText().toString().trim();
        String password = mLoginPasswordText.getText().toString().trim();
        saveLoginInfo(account, password);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        DataCacheManager.buildDataCacheAsync();
        finish();
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

}
