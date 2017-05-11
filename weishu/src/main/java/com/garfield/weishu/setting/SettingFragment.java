package com.garfield.weishu.setting;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.garfield.baselib.ui.widget.SwitchButton;
import com.garfield.baselib.utils.system.L;
import com.garfield.baselib.utils.system.SharedPreferencesUtil;
import com.garfield.baselib.utils.system.ThemeManager;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.app.SettingsPreferences;
import com.garfield.weishu.nim.NimConfig;
import com.garfield.weishu.nim.RegisterAndLogin;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.garfield.weishu.ui.activity.ThemeMaskActivity;
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

    @BindView(R.id.fragment_setting_night_switch)
    SwitchButton mNightSwitch;

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
    }

    @Override
    protected void onLazyLoad() {
        registerObservers(true);
        loadUserInfo();
        loadConfig();
    }

    private void loadConfig() {
        mNotifySwitch.setSwitchStatus(SettingsPreferences.getNotificationToggle());
        mRingSwitch.setSwitchStatus(SettingsPreferences.getRingToggle());
        mVibrateSwitch.setSwitchStatus(SettingsPreferences.getVibrateToggle());
        mAnimatorSwitch.setSwitchStatus(SettingsPreferences.getAnimation());
        mNightSwitch.setSwitchStatus(SharedPreferencesUtil.getBoolean("night_mode"));

        int visible = mNotifySwitch.getSwitchStatus() ? View.VISIBLE : View.GONE;
        mRingContainer.setVisibility(visible);
        mVibrateContainer.setVisibility(visible);
    }

    private void loadUserInfo() {
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
                loadUserInfo();
            }
        }
    };

    @OnClick({R.id.fragment_setting_notify,
              R.id.fragment_setting_ring,
              R.id.fragment_setting_vibrate,
              R.id.fragment_setting_animator,
              R.id.fragment_setting_night,
              R.id.fragment_setting_clear_message,
              R.id.fragment_setting_about,
              R.id.fragment_setting_logout})
    void switchSetting(View view) {
        switch(view.getId()) {
            case R.id.fragment_setting_notify:
                mNotifySwitch.setSwitchStatus(!mNotifySwitch.getSwitchStatus());
                SettingsPreferences.setNotificationToggle(mNotifySwitch.getSwitchStatus());
                int visible = mNotifySwitch.getSwitchStatus() ? View.VISIBLE : View.GONE;
                mRingContainer.setVisibility(visible);
                mVibrateContainer.setVisibility(visible);
                NimConfig.toggleNotification(mNotifySwitch.getSwitchStatus());
                break;
            case R.id.fragment_setting_ring:
                mRingSwitch.setSwitchStatus(!mRingSwitch.getSwitchStatus());
                SettingsPreferences.setRingToggle(mRingSwitch.getSwitchStatus());
                NimConfig.setRingToggle(mRingSwitch.getSwitchStatus());
                break;
            case R.id.fragment_setting_vibrate:
                mVibrateSwitch.setSwitchStatus(!mVibrateSwitch.getSwitchStatus());
                SettingsPreferences.setVibrateToggle(mVibrateSwitch.getSwitchStatus());
                NimConfig.setVibrateToggle(mVibrateSwitch.getSwitchStatus());
                break;
            case R.id.fragment_setting_animator:
                mAnimatorSwitch.setSwitchStatus(!mAnimatorSwitch.getSwitchStatus());
                SettingsPreferences.setAnimation(mAnimatorSwitch.getSwitchStatus());
                AppCache.setHasAnimation(mAnimatorSwitch.getSwitchStatus());
                break;
            case R.id.fragment_setting_night:
                if (mNightSwitch.getSwitchStatus()) {
                    AppCache.setNightMode(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    SharedPreferencesUtil.saveBoolean("night_mode", false);
                    ThemeMaskActivity.start(mActivity, false);
                } else {
                    AppCache.setNightMode(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    SharedPreferencesUtil.saveBoolean("night_mode", true);
                    ThemeMaskActivity.start(mActivity, true);
                }
                break;
            case R.id.fragment_setting_clear_message:
                MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                        .backgroundColorRes(R.color.bg_itemFragment)
                        .title(R.string.is_clear_message_record)
                        .positiveText(R.string.confirm)
                        .positiveColor(getResources().getColor(R.color.mainTextColor))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                NIMClient.getService(MsgService.class).clearMsgDatabase(true);
                                L.toast(R.string.clear_message_record_done);
                            }
                        })
                        .negativeText(R.string.cancel)
                        .negativeColor(getResources().getColor(R.color.mainTextColor))
                        .build();
                EventDispatcher.startDialog(dialog);
                break;
            case R.id.fragment_setting_about:
                EventDispatcher.startFragmentEvent(new AboutFragment());
                break;
            case R.id.fragment_setting_logout:
                dialog = new MaterialDialog.Builder(getContext())
                        .backgroundColorRes(R.color.bg_itemFragment)
                        .title(R.string.logout_confirm)
                        .positiveText(R.string.confirm)
                        .positiveColor(getResources().getColor(R.color.mainTextColor))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // 只有手动退出时调用
                                NIMClient.getService(AuthService.class).logout();
                                RegisterAndLogin.logout(mActivity);
                            }
                        })
                        .negativeText(R.string.cancel)
                        .negativeColor(getResources().getColor(R.color.mainTextColor))
                        .build();
                EventDispatcher.startDialog(dialog);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registerObservers(false);
    }
}
