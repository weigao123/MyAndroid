package com.garfield.weishu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.garfield.weishu.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.config.UserPreferences;
import com.garfield.weishu.event.StartBrotherEvent;
import com.garfield.weishu.nim.RegisterAndLogin;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.garfield.weishu.ui.activity.WelcomeActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gwball on 2016/8/2.
 */
public class SettingFragment extends AppBaseFragment {

    @BindView(R.id.fragment_setting_head)
    ImageView mHeadImage;

    @BindView(R.id.fragment_setting_nickname)
    TextView mNickNameText;

    @BindView(R.id.fragment_setting_account)
    TextView mAccountText;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mHeadImage.setImageResource(R.drawable.default_avatar);
        mAccountText.setText(getString(R.string.weishu_account_is, AppCache.getAccount()));

        mNickNameText.setText(UserInfoCache.getInstance().getUserName(AppCache.getAccount()));

    }


    @OnClick(R.id.fragment_setting_logout)
    void logout() {
        // 只有手动退出时调用
        NIMClient.getService(AuthService.class).logout();
        RegisterAndLogin.logout(mActivity);
    }

    @OnClick(R.id.fragment_setting_user_info)
    void modifyUserInfo() {
        EventBus.getDefault().post(new StartBrotherEvent(new SelfProfileFragment()));

    }
}
