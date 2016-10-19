package com.garfield.weishu.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.garfield.baselib.ui.dialog.DialogMaker;
import com.garfield.baselib.utils.InvokerUtils;
import com.garfield.baselib.utils.L;
import com.garfield.weishu.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.garfield.weishu.nim.cache.UserUpdateHelper;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.nos.NosService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

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
    ImageView mHeadImageView;

    @BindView(R.id.fragment_self_nickname)
    TextView mNickNameText;

    @BindView(R.id.fragment_self_weishu_account)
    TextView mWeishuAccount;

    private AbortableFuture<String> uploadAvatarFuture;
    private Handler mHandler = new Handler();

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_self_profile;
    }

    @Override
    protected int onGetToolbarTitleResource() {
        return R.string.self_details;
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

    @OnClick(R.id.fragment_self_container_head)
    void changeHeadImage() {
        EventDispatcher.getFragmentJumpEvent().onShowTakePhoto();
    }

    private void refreshInfo() {
        NimUserInfo userInfo = UserInfoCache.getInstance().getUserInfoByAccount(AppCache.getAccount());
        ImageLoader.getInstance().displayImage(userInfo.getAvatar(), mHeadImageView);
        mNickNameText.setText(userInfo.getName());
        mWeishuAccount.setText(AppCache.getAccount());
    }

    private void registerObservers(boolean register) {
        UserInfoCache.getInstance().registerUserInfoChangedObserver(new UserInfoCache.UserInfoChangedObserver() {
            @Override
            public void onUserInfoChanged(List<String> accounts) {
                if (accounts.contains(AppCache.getAccount())) {
                    refreshInfo();
                }
            }
        }, register);
    }

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
                        Toast.makeText(AppCache.getContext(), R.string.head_upload_failed, Toast.LENGTH_SHORT).show();
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
        UserUpdateHelper.update(type, data, new RequestCallbackWrapper<Void>() {
            @Override
            public void onResult(int code, Void result, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS) {
                    Toast.makeText(AppCache.getContext(), R.string.user_info_update_success, Toast.LENGTH_SHORT).show();
                    onUpdateDone();
                } else {
                    Toast.makeText(AppCache.getContext(), R.string.user_info_update_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cancelUpload(int resId) {
        if (uploadAvatarFuture != null) {
            uploadAvatarFuture.abort();
            Toast.makeText(AppCache.getContext(), resId, Toast.LENGTH_SHORT).show();
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
