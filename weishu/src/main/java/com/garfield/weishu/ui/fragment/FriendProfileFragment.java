package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.garfield.baselib.ui.dialog.DialogMaker;
import com.garfield.baselib.utils.NetworkUtil;
import com.garfield.weishu.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.nim.cache.FriendDataCache;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import butterknife.BindView;
import butterknife.OnClick;

import static com.garfield.weishu.AppCache.USER_ACCOUNT;

/**
 * Created by gwball on 2016/9/12.
 */
public class FriendProfileFragment extends AppBaseFragment {

    private String mAccount;

    @BindView(R.id.fragment_friend_profile_head)
    ImageView mHeadImage;

    @BindView(R.id.fragment_friend_profile_account)
    TextView mAccountText;

    @BindView(R.id.fragment_friend_profile_nickname)
    TextView mNickNameText;

    @BindView(R.id.fragment_friend_profile_chat)
    TextView mChatBtn;

    @BindView(R.id.fragment_friend_profile_add_or_remove)
    TextView mAddOrRemoveFriendBtn;

    public static FriendProfileFragment newInstance(String account) {
        Bundle args = new Bundle();
        args.putString(USER_ACCOUNT, account);
        FriendProfileFragment fragment = new FriendProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_friend_profile;
    }

    @Override
    protected boolean onEnableSwipe() {
        return true;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mAccount = getArguments().getString(USER_ACCOUNT);
        updateOperatorView(FriendDataCache.getInstance().isMyFriend(mAccount));
        updateUserInfoView(UserInfoCache.getInstance().getUserInfoByAccount(mAccount));
    }

    @Override
    protected int onGetToolbarTitleResource() {
        return R.string.details;
    }


    private void updateUserInfoView(NimUserInfo userInfo) {
        mHeadImage.setImageResource(R.drawable.default_avatar);
        mAccountText.setText(getString(R.string.weishu_account_is, userInfo.getAccount()));
        if (TextUtils.isEmpty(userInfo.getName())) {
            mNickNameText.setText("无昵称");
        } else {
            mNickNameText.setText(userInfo.getName());
        }

    }

    @OnClick(R.id.fragment_friend_profile_add_or_remove)
    void onAddFriendBtnClick() {
        if (FriendDataCache.getInstance().isMyFriend(mAccount)) {
            doRemoveFriend();
        } else {
            doAddFriend("", true);
        }
    }

    private void doAddFriend(String msg, boolean addDirectly) {
        if (!NetworkUtil.isNetAvailable(mActivity)) {
            Toast.makeText(mActivity, R.string.status_network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(mAccount) && mAccount.equals(AppCache.getAccount())) {
            Toast.makeText(mActivity, R.string.can_not_add_self, Toast.LENGTH_SHORT).show();
            return;
        }
        final VerifyType verifyType = addDirectly ? VerifyType.DIRECT_ADD : VerifyType.VERIFY_REQUEST;
        DialogMaker.showProgressDialog(mActivity, null, true);
        NIMClient.getService(FriendService.class).addFriend(new AddFriendData(mAccount, verifyType, msg)).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        DialogMaker.dismissProgressDialog();
                        updateOperatorView(true);
                        if (VerifyType.DIRECT_ADD == verifyType) {
                            Toast.makeText(mActivity, R.string.add_friend_success, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mActivity, R.string.add_friend_request, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                        if (code == 408) {
                            Toast.makeText(mActivity, R.string.status_network_is_not_available, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mActivity, "on failed:" + code, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                });
    }


    private void doRemoveFriend() {
        if (!NetworkUtil.isNetAvailable(mActivity)) {
            Toast.makeText(mActivity, R.string.status_network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        DialogMaker.showProgressDialog(mActivity, null, true);

        NIMClient.getService(FriendService.class).deleteFriend(mAccount).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                DialogMaker.dismissProgressDialog();
                Toast.makeText(mActivity, R.string.remove_friend_success, Toast.LENGTH_SHORT).show();
                updateOperatorView(false);
            }

            @Override
            public void onFailed(int code) {
                DialogMaker.dismissProgressDialog();
                if (code == 408) {
                    Toast.makeText(mActivity, R.string.status_network_is_not_available, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, "on failed:" + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
            }
        });

    }


    private void updateOperatorView(boolean isFriend) {
        if (isFriend) {
            mChatBtn.setVisibility(View.VISIBLE);
            mAddOrRemoveFriendBtn.setText(R.string.remove_friend);
        } else {
            mChatBtn.setVisibility(View.GONE);
            mAddOrRemoveFriendBtn.setText(R.string.add_friend);
        }
    }
}
