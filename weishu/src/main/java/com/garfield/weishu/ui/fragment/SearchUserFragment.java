package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.garfield.baselib.ui.dialog.DialogMaker;
import com.garfield.baselib.ui.widget.ClearableEditText;
import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaowei3 on 2016/9/9.
 */
public class SearchUserFragment extends AppBaseFragment {

    @BindView(R.id.fragment_search_user_account)
    ClearableEditText mClearableEditText;

    @BindView(R.id.fragment_search_user_search)
    TextView mSearchButton;

    @BindView(R.id.fragment_search_user_result)
    TextView mSearchResult;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_search_user;
    }

    @Override
    protected int onGetToolbarTitleResource() {
        return R.string.add_new_friend;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);

    }

    @OnClick(R.id.fragment_search_user_search)
    void searchNewUser() {
        DialogMaker.showProgressDialog(mActivity, null, false);
        UserInfoCache.getInstance().getUserInfoFromRemote(mClearableEditText.getText().toString().toLowerCase(), new RequestCallback<NimUserInfo>() {
            @Override
            public void onSuccess(NimUserInfo userInfo) {
                DialogMaker.dismissProgressDialog();
                if (userInfo == null) {
                    Toast.makeText(mActivity, "该用户不存在", Toast.LENGTH_SHORT).show();
                } else {
                    EventDispatcher.getFragmentJumpEvent().onShowUserProfile(userInfo.getAccount());
                }
            }

            @Override
            public void onFailed(int code) {
                DialogMaker.dismissProgressDialog();
                if (code == 408) {
                    Toast.makeText(mActivity, "网络不可用", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, "on failed:" + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable throwable) {
                DialogMaker.dismissProgressDialog();
                Toast.makeText(mActivity, "on exception:" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
