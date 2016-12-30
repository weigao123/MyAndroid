package com.garfield.weishu.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.garfield.baselib.ui.widget.ClearableEditText;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.R;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

import static com.garfield.weishu.setting.SelfProfileFragment.INFO_NAME;

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
    protected String onGetToolbarTitleResource() {
        return getString(R.string.change_name);
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
        Bundle bundle = new Bundle();
        bundle.putString(INFO_NAME, name);
        setFragmentResult(bundle);
        popToFragment(SelfProfileFragment.class, false);
    }
}
