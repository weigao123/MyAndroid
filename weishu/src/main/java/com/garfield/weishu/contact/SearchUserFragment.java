package com.garfield.weishu.contact;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.garfield.baselib.ui.dialog.DialogMaker;
import com.garfield.baselib.ui.widget.ClearableEditText;
import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.garfield.weishu.ui.fragment.AppBaseFragment;
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
    protected String onGetToolbarTitle() {
        return getString(R.string.add_new_friend);
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
                    L.toast("该用户不存在");
                } else {
                    EventDispatcher.getFragmentJumpEvent().onShowUserProfile(userInfo.getAccount());
                }
            }

            @Override
            public void onFailed(int code) {
                DialogMaker.dismissProgressDialog();
                if (code == 408) {
                    L.toast("网络不可用");
                } else {
                    L.toast("on failed:" + code);
                }
            }

            @Override
            public void onException(Throwable throwable) {
                DialogMaker.dismissProgressDialog();
                L.toast("on exception:" + throwable.getMessage());
            }
        });
    }


}
