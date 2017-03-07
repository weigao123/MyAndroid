package com.garfield.weishu.developer.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import com.garfield.baselib.utils.system.SystemUtil;
import com.garfield.weishu.R;
import com.garfield.weishu.ui.activity.AppBaseActivity;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2017/2/22.
 */

public class DeveloperActivity extends AppBaseActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected int onGetActivityLayout() {
        return R.layout.activity_develop;
    }

    @Override
    protected void onInitViewAndData(Bundle savedInstanceState) {
        super.onInitViewAndData(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // 设置状态栏透明
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }

        SystemUtil.setStatusBarColor(this, getResources().getColor(R.color.colorPrimaryDark));

        mDrawerLayout.setFitsSystemWindows(false);
    }



}
