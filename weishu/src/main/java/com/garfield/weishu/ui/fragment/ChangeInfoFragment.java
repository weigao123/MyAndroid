package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.garfield.baselib.ui.widget.ClearableEditText;
import com.garfield.weishu.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaowei3 on 2016/9/21.
 */

public class ChangeInfoFragment extends AppBaseFragment {

    @BindView(R.id.fragment_change_info_name)
    ClearableEditText mToChangeText;

    @BindView(R.id.confirm)
    TextView mConfirm;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_change_info;
    }

    @Override
    protected int onGetToolbarTitleResource() {
        return R.string.change_name;
    }

    @Override
    protected boolean onEnableSwipe() {
        return true;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mToChangeText.setText(UserInfoCache.getInstance().getUserName(AppCache.getAccount()));
        mConfirm.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.confirm)
    void changeBtn() {
        String name = mToChangeText.getText().toString();
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        fields.put(UserInfoFieldEnum.Name, name);
        NIMClient.getService(UserService.class).updateUserInfo(fields).setCallback(new RequestCallbackWrapper<Void>() {
            @Override
            public void onResult(int code, Void result, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS) {
                    Toast.makeText(AppCache.getContext(), R.string.update_success, Toast.LENGTH_SHORT).show();
                    popFragment();
                } else {
                    Toast.makeText(AppCache.getContext(), R.string.update_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
