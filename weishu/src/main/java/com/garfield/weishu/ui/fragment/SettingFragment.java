package com.garfield.weishu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.weishu.R;
import com.garfield.weishu.config.UserPreferences;
import com.garfield.weishu.ui.activity.LoginActivity;
import com.garfield.weishu.ui.activity.WelcomeActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

import butterknife.OnClick;

/**
 * Created by gwball on 2016/8/2.
 */
public class SettingFragment extends AppBaseFragment {

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
    }


    @OnClick(R.id.fragment_setting_logout)
    void logout() {
        NIMClient.getService(AuthService.class).logout();
        UserPreferences.saveUserToken("");
        startActivity(new Intent(mActivity, WelcomeActivity.class));
        mActivity.finish();
        //mActivity.overridePendingTransition(0, 0);
    }
}
