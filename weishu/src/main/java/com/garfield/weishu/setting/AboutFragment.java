package com.garfield.weishu.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.garfield.baselib.ui.dialog.DialogMaker;
import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.nim.RegisterAndLogin;
import com.garfield.weishu.nim.cache.FriendDataCache;
import com.garfield.weishu.ui.fragment.AppBaseFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

/**
 * Created by gaowei3 on 2016/11/30.
 */

public class AboutFragment extends AppBaseFragment {

    @BindView(R.id.fragment_about_version)
    TextView mVersionText;

    @BindView(R.id.fragment_about_me)
    TextView mAboutMe;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_about;
    }

    @Override
    protected String onGetToolbarTitleResource() {
        return getString(R.string.about_weishu);
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);

        String version;
        try {
            PackageInfo pi = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_META_DATA);
            version = pi.versionName + "." +pi.versionCode;
        } catch (Exception e) {
            version = "";
        }
        mVersionText.setText(getString(R.string.weishu_version, version));
    }

    @OnClick(R.id.fragment_about_me)
    void addFriend() {
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .title(R.string.add_author)
                .positiveText(R.string.confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // 只有手动退出时调用
                        doAddFriend();
                    }
                })
                .negativeText(R.string.cancel)
                .build();
        EventDispatcher.startDialog(dialog);
    }

    private void doAddFriend() {
        if (FriendDataCache.getInstance().isMyFriend("gwblue")) {
            L.toast(R.string.author_is_your_friend);
            return;
        }
        DialogMaker.showProgressDialog(mActivity, null, true);
        NIMClient.getService(FriendService.class).addFriend(new AddFriendData("gwblue", VerifyType.DIRECT_ADD, "")).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                DialogMaker.dismissProgressDialog();
                L.toast(R.string.add_friend_success);
            }

            @Override
            public void onFailed(int code) {
                DialogMaker.dismissProgressDialog();
                if (code == 408) {
                    L.toast(R.string.status_network_is_not_available);
                } else {
                    L.toast("on failed:" + code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
            }
        });

    }
}
