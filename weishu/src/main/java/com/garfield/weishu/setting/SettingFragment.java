package com.garfield.weishu.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.garfield.baselib.ui.widget.SwitchButton;
import com.garfield.weishu.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.config.SettingsPreferences;
import com.garfield.weishu.nim.RegisterAndLogin;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.garfield.weishu.ui.fragment.AppBaseFragment;
import com.garfield.weishu.ui.view.HeadImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

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
    SwitchButton mNotifySwitch;

    @BindView(R.id.fragment_setting_ring_switch)
    SwitchButton mRingSwitch;

    @BindView(R.id.fragment_setting_vibrate_switch)
    SwitchButton mVibrateSwitch;

    @BindView(R.id.fragment_setting_animator_switch)
    SwitchButton mAnimatorSwitch;

    @BindView(R.id.fragment_setting_native_crop_switch)
    SwitchButton mCropSwitch;

    @BindView(R.id.fragment_setting_ring)
    RelativeLayout mRingContainer;

    @BindView(R.id.fragment_setting_vibrate)
    RelativeLayout mVibrateContainer;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        registerObservers(true);
        initConfig();
        refreshInfo();
    }

    private void initConfig() {
        mNotifySwitch.setSwitchStatus(SettingsPreferences.getNotificationToggle());
        mRingSwitch.setSwitchStatus(SettingsPreferences.getRingToggle());
        mVibrateSwitch.setSwitchStatus(SettingsPreferences.getVibrateToggle());
        mAnimatorSwitch.setSwitchStatus(SettingsPreferences.getAnimation());
        mCropSwitch.setSwitchStatus(SettingsPreferences.getCropTool());

        int visible = mNotifySwitch.getSwitchStatus() ? View.VISIBLE : View.GONE;
        mRingContainer.setVisibility(visible);
        mVibrateContainer.setVisibility(visible);
        NIMClient.toggleNotification(mNotifySwitch.getSwitchStatus());

        AppCache.setHasAnimation(mAnimatorSwitch.getSwitchStatus());
    }

    private void refreshInfo() {
        NimUserInfo userInfo = UserInfoCache.getInstance().getUserInfoByAccount(AppCache.getAccount());
        mHeadImage.loadBuddyAvatar(userInfo.getAccount());
        mAccountText.setText(getString(R.string.weishu_account_is, userInfo.getAccount()));
        mNickNameText.setText(userInfo.getName());
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
        mNotifySwitch.setSwitchStatus(!mNotifySwitch.getSwitchStatus());
        NIMClient.toggleNotification(mNotifySwitch.getSwitchStatus());
        SettingsPreferences.setNotificationToggle(mNotifySwitch.getSwitchStatus());
        int visible = mNotifySwitch.getSwitchStatus() ? View.VISIBLE : View.GONE;
        mRingContainer.setVisibility(visible);
        mVibrateContainer.setVisibility(visible);
    }

    @OnClick(R.id.fragment_setting_ring)
    void switchRing() {
        mRingSwitch.setSwitchStatus(!mRingSwitch.getSwitchStatus());
        SettingsPreferences.setRingToggle(mRingSwitch.getSwitchStatus());
    }

    @OnClick(R.id.fragment_setting_vibrate)
    void switchVibrate() {
        mVibrateSwitch.setSwitchStatus(!mVibrateSwitch.getSwitchStatus());
        SettingsPreferences.setVibrateToggle(mVibrateSwitch.getSwitchStatus());
    }

    @OnClick(R.id.fragment_setting_animator)
    void switchAnimator() {
        mAnimatorSwitch.setSwitchStatus(!mAnimatorSwitch.getSwitchStatus());
        AppCache.setHasAnimation(mAnimatorSwitch.getSwitchStatus());
        SettingsPreferences.setAnimation(mAnimatorSwitch.getSwitchStatus());
    }

    @OnClick(R.id.fragment_setting_native_crop)
    void switchCropTool() {
        mCropSwitch.setSwitchStatus(!mCropSwitch.getSwitchStatus());
        SettingsPreferences.setCropTool(mCropSwitch.getSwitchStatus());
    }

    @OnClick(R.id.fragment_setting_clear_message)
    void clearMessage() {
        new MaterialDialog.Builder(getContext())
                .title(R.string.is_clear_message_record)
                .positiveText(R.string.confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        NIMClient.getService(MsgService.class).clearMsgDatabase(true);
                        Toast.makeText(mActivity, R.string.clear_message_record_done, Toast.LENGTH_SHORT).show();
                    }
                })
                .negativeText(R.string.cancel)
                .build()
                .show();
    }

    @OnClick(R.id.fragment_setting_about)
    void showAbout() {

    }

    @OnClick(R.id.fragment_setting_logout)
    void logout() {
        new MaterialDialog.Builder(getContext())
                .title(R.string.logout_confirm)
                .positiveText(R.string.confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // 只有手动退出时调用
                        NIMClient.getService(AuthService.class).logout();
                        RegisterAndLogin.logout(mActivity);
                    }
                })
                .negativeText(R.string.cancel)
                .build()
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registerObservers(false);
    }

}
