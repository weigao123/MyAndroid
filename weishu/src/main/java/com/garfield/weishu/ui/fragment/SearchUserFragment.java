package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.garfield.baselib.ui.widget.ClearableEditText;
import com.garfield.weishu.R;
import com.garfield.weishu.nim.ContactHttpClient;
import com.garfield.weishu.nim.Test;
import com.garfield.weishu.nim.UserInfoUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
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

        return;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Test.main();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();


//        ContactHttpClient.getInstance().register("gwball3", "jfm", "111111", new ContactHttpClient.ContactHttpCallback<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(mActivity, "成功", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onFailed(int code, String errorMsg) {
//                Toast.makeText(mActivity, "失败:"+code, Toast.LENGTH_SHORT).show();
//
//            }
//        });


//        UserInfoUtils.getUserInfoFromRemote(mClearableEditText.getText().toString().toLowerCase(), new RequestCallback<NimUserInfo>() {
//            @Override
//            public void onSuccess(NimUserInfo nimUserInfo) {
//                mSearchResult.setText(nimUserInfo.getAccount());
//            }
//
//            @Override
//            public void onFailed(int i) {
//
//            }
//
//            @Override
//            public void onException(Throwable throwable) {
//
//            }
//        });
    }


}
