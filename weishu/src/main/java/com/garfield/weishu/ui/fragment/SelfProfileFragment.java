package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.garfield.weishu.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.event.StartBrotherEvent;
import com.garfield.weishu.nim.cache.UserInfoCache;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaowei3 on 2016/9/21.
 */

public class SelfProfileFragment extends AppBaseFragment {

    @BindView(R.id.fragment_self_nickname)
    TextView mNickNameText;

    @BindView(R.id.fragment_self_weishu_account)
    TextView mWeishuText;

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
        refreshInfo();
    }

    @OnClick(R.id.fragment_self_container_nickname)
    void changeName() {
        EventBus.getDefault().post(new StartBrotherEvent(new ChangeInfoFragment()));
    }

    @OnClick(R.id.fragment_self_container_head)
    void changeHeadImage() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            refreshInfo();
        }
    }

    private void refreshInfo() {
        mNickNameText.setText(UserInfoCache.getInstance().getUserName(AppCache.getAccount()));
        mWeishuText.setText(AppCache.getAccount());
    }
}
