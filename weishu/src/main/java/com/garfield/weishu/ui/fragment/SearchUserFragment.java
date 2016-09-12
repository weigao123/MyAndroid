package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.garfield.baselib.ui.widget.ClearableEditText;
import com.garfield.weishu.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.List;

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
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);

    }

    @Override
    protected int onGetToolbarTitleResource() {
        return R.string.add_new_friend;
    }

    @OnClick(R.id.fragment_search_user_search)
    void searchNewUser() {

        List<NimUserInfo> users = NIMClient.getService(UserService.class).getAllUserInfo();




    }


}
