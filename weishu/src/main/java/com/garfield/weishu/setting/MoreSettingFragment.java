package com.garfield.weishu.setting;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.garfield.baselib.ui.widget.SwitchButton;
import com.garfield.weishu.R;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.app.SettingsPreferences;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.nim.RegisterAndLogin;
import com.garfield.weishu.ui.fragment.AppBaseFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaowei on 2017/5/19.
 */

public class MoreSettingFragment extends AppBaseFragment {

    @BindView(R.id.more_setting_animator_switch)
    SwitchButton mAnimatorSwitch;

    @BindView(R.id.more_setting_close_background_switch)
    SwitchButton mCloseToBgSwitch;

    @Override
    protected String onGetToolbarTitle() {
        return "设置";
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_more_setting;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mAnimatorSwitch.setSwitchStatus(SettingsPreferences.getAnimation());
        mCloseToBgSwitch.setSwitchStatus(SettingsPreferences.getCloseBg());
    }

    @OnClick({R.id.more_setting_animator,
              R.id.more_setting_close_background,
              R.id.more_setting_logout})
    void changeSetting(View view) {
        switch (view.getId()) {
            case R.id.more_setting_animator:
                mAnimatorSwitch.setSwitchStatus(!mAnimatorSwitch.getSwitchStatus());
                SettingsPreferences.setAnimation(mAnimatorSwitch.getSwitchStatus());
                AppCache.setHasAnimation(mAnimatorSwitch.getSwitchStatus());
                break;
            case R.id.more_setting_close_background:
                mCloseToBgSwitch.setSwitchStatus(!mCloseToBgSwitch.getSwitchStatus());
                SettingsPreferences.setCloseBg(mCloseToBgSwitch.getSwitchStatus());
                AppCache.setCloseToBg(mCloseToBgSwitch.getSwitchStatus());
                break;
            case R.id.more_setting_logout:
                Dialog dialog = new MaterialDialog.Builder(getContext())
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
}
