package com.garfield.weishu.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.garfield.baselib.fragmentation.SupportActivity;
import com.garfield.weishu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaowei3 on 2016/8/25.
 */

/**
 * 要使用ButterKnife，就要重写一些方法
 */
public class AppBaseActivity extends SupportActivity {

    @Nullable @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Nullable @BindView(R.id.toolbar_control_view)
    LinearLayout mToolbarControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (onGetActivityLayout() != 0) {
            setContentView(onGetActivityLayout());
            ButterKnife.bind(this);
            if (mToolbar != null) {
                mToolbar.setTitle(onGetToolbarTitleResource());
                mToolbar.setTitleTextAppearance(this, R.style.toolbar_text);
            }
            onInitViewAndData(savedInstanceState);
        }
    }

    protected int onGetActivityLayout() {
        return 0;
    }

    protected void onInitViewAndData(Bundle savedInstanceState) {

    }

    protected int onGetToolbarTitleResource() {
        return R.string.app_name;
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

}
