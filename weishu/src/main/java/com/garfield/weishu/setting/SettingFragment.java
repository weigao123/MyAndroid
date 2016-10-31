package com.garfield.weishu.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.garfield.baselib.ui.widget.SwitchButton;
import com.garfield.weishu.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.nim.RegisterAndLogin;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.garfield.weishu.ui.fragment.AppBaseFragment;
import com.garfield.weishu.ui.view.HeadImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gwball on 2016/8/2.
 */
public class SettingFragment extends AppBaseFragment {

    @BindView(R.id.fragment_setting_head)
    HeadImageView mHeadImage;

    @BindView(R.id.fragment_setting_nickname)
    TextView mNickNameText;

    @BindView(R.id.fragment_setting_account)
    TextView mAccountText;

    @BindView(R.id.fragment_setting_notify_switch)
    SwitchButton mNofitySwitch;

    @BindView(R.id.fragment_setting_sound_switch)
    SwitchButton mSoundSwitch;

    @BindView(R.id.fragment_setting_vibrate_switch)
    SwitchButton mVibrateSwitch;

    @BindView(R.id.fragment_setting_animator_switch)
    SwitchButton mAnimatorSwitch;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        registerObservers(true);
        refreshInfo();
    }

    private void refreshInfo() {
        NimUserInfo userInfo = UserInfoCache.getInstance().getUserInfoByAccount(AppCache.getAccount());
        mHeadImage.loadBuddyAvatar(userInfo.getAccount());
        mAccountText.setText(getString(R.string.weishu_account_is, userInfo.getAccount()));
        mNickNameText.setText(userInfo.getName());
    }

    @OnClick(R.id.fragment_setting_logout)
    void logout() {
        // 只有手动退出时调用
        NIMClient.getService(AuthService.class).logout();
        RegisterAndLogin.logout(mActivity);
    }

    @OnClick(R.id.fragment_setting_user_info)
    void modifyUserInfo() {
        EventDispatcher.getFragmentJumpEvent().onShowSelfProfile();
    }

    private void registerObservers(boolean register) {
        UserInfoCache.getInstance().registerUserInfoChangedObserver(mUserInfoChangedObserver, register);
    }

    private UserInfoCache.UserInfoChangedObserver mUserInfoChangedObserver = new UserInfoCache.UserInfoChangedObserver() {
        @Override
        public void onUserInfoChanged(List<String> accounts) {
            if (accounts.contains(AppCache.getAccount())) {
                refreshInfo();
            }
        }
    };


    @OnClick(R.id.fragment_setting_notify)
    void switchNofity() {

    }
    @OnClick(R.id.fragment_setting_sound)
    void switchSound() {

    }
    @OnClick(R.id.fragment_setting_vibrate)
    void switchVibrate() {

    }
    @OnClick(R.id.fragment_setting_animator)
    void switchAnimator() {

    }
    @OnClick(R.id.fragment_setting_clear_message)
    void clearMessage() {

    }
    @OnClick(R.id.fragment_setting_about)
    void showAbout() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registerObservers(false);
    }

}
