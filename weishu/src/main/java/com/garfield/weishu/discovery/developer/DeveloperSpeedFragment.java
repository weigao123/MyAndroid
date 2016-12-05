package com.garfield.weishu.discovery.developer;

import android.os.Bundle;
import android.view.View;

import com.garfield.baselib.utils.system.SystemUtil;
import com.garfield.weishu.R;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

/**
 * Created by gaowei3 on 2016/11/30.
 */

public class DeveloperSpeedFragment extends AppBaseFragment {

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_developer_speed;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        SystemUtil.setStatusColor(mActivity, getResources().getColor(R.color.black));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SystemUtil.setStatusColor(mActivity, getResources().getColor(R.color.colorPrimary));
    }
}
