package com.garfield.weishu.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.garfield.baselib.ui.dialog.DialogMaker;
import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.garfield.weishu.nim.NimHelper;
import com.garfield.weishu.ui.fragment.AppBaseFragment;
import com.garfield.weishu.ui.view.HeadImageView;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.nos.NosService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaowei3 on 2016/9/21.
 */

public class SelfProfileFragment extends AppBaseFragment {

    private static final int AVATAR_TIME_OUT = 30000;
    public static final String INFO_HEAD = "info_head";
    public static final String INFO_NAME = "info_name";

    @BindView(R.id.fragment_self_head_img)
    HeadImageView mHeadImageView;

    @BindView(R.id.fragment_self_nickname)
    TextView mNickNameText;

    @BindView(R.id.fragment_self_weishu_account)
    TextView mWeishuAccount;

    private NimUserInfo mUserInfo;
    private AbortableFuture<String> uploadAvatarFuture;
    private Handler mHandler = new Handler();

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_self_profile;
    }

    @Override
    protected String onGetToolbarTitleResource() {
        return getString(R.string.self_details);
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        registerObservers(true);
        refreshInfo();
    }

    @OnClick(R.id.fragment_self_container_nickname)
    void changeName() {
        EventDispatcher.getFragmentJumpEvent().onShowChangeInfo();
    }

    @OnClick({R.id.fragment_self_container_head, R.id.fragment_self_head_img})
    void changeHeadImage(View view) {
        if (view.getId() == R.id.fragment_self_container_head) {
            EventDispatcher.getFragmentJumpEvent().onShowTakePhoto();
        } else if (view.getId() == R.id.fragment_self_head_img) {
            EventDispatcher.getFragmentJumpEvent().onShowFullscreenPhoto(mUserInfo.getAvatar());
        }
    }

    private void refreshInfo() {
        mUserInfo = UserInfoCache.getInstance().getUserInfoByAccount(AppCache.getAccount());
        mHeadImageView.loadBuddyAvatar(mUserInfo.getAccount());
        mNickNameText.setText(mUserInfo.getName());
        mWeishuAccount.setText(mUserInfo.getAccount());
    }

    private void registerObservers(boolean register) {
        UserInfoCache.getInstance().registerUserInfoChangedObserver(mUserInfoChangedObserver, register);
    }

    /**
     * 必须要设置成全局变量，如果在registerObservers里新建，会导致新new一个observer，之前的无法被remove
     * 而之前的Fragment已经被销毁，一旦回调，就空指针
     */
    private UserInfoCache.UserInfoChangedObserver mUserInfoChangedObserver = new UserInfoCache.UserInfoChangedObserver() {
        @Override
        public void onUserInfoChanged(List<String> accounts) {
            if (accounts.contains(AppCache.getAccount())) {
                refreshInfo();
            }
        }
    };

    @Override
    protected void onFragmentResult(Bundle data) {
        DialogMaker.showProgressDialog(mActivity, null, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelUpload(R.string.user_info_update_cancel);
            }
        }).setCanceledOnTouchOutside(false);

        final String photoPath = data.getString(INFO_HEAD);
        final String name = data.getString(INFO_NAME);
        if (photoPath != null) {
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            String type = fileNameMap.getContentTypeFor(photoPath);
            /**
             * 要求这里的file没有前缀file://,直接是/storage/...即可
             */
            File file = new File(photoPath);
            mHandler.postDelayed(outimeTask, AVATAR_TIME_OUT);
            uploadAvatarFuture = NIMClient.getService(NosService.class).upload(file, type);
            uploadAvatarFuture.setCallback(new RequestCallbackWrapper<String>() {
                @Override
                public void onResult(int code, String url, Throwable exception) {
                    L.d("image_upload: "+url);
                    if (code == ResponseCode.RES_SUCCESS && !TextUtils.isEmpty(url)) {
                        updateUserInfo(UserInfoFieldEnum.AVATAR, url);
                    } else {
                        L.toast(R.string.head_upload_failed);
                        onUpdateDone();
                    }
                }
            });
        }
        if (name != null) {
            updateUserInfo(UserInfoFieldEnum.Name, name);
        }
    }

    private void updateUserInfo(UserInfoFieldEnum type, String data) {
        NimHelper.updateUserInfo(type, data, new RequestCallbackWrapper<Void>() {
            @Override
            public void onResult(int code, Void result, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS) {
                    L.toast(R.string.user_info_update_success);
                    onUpdateDone();
                } else {
                    L.toast(R.string.user_info_update_failed);
                }
            }
        });
    }

    private void cancelUpload(int resId) {
        if (uploadAvatarFuture != null) {
            uploadAvatarFuture.abort();
            L.toast(resId);
            onUpdateDone();
        }
    }

    private Runnable outimeTask = new Runnable() {
        @Override
        public void run() {
            cancelUpload(R.string.user_info_update_failed);
        }
    };

    private void onUpdateDone() {
        uploadAvatarFuture = null;
        mHandler.removeCallbacksAndMessages(null);
        DialogMaker.dismissProgressDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registerObservers(false);
    }
}
