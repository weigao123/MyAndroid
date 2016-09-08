package com.garfield.weishu.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.garfield.baselib.fragmentation.SupportActivity;
import com.garfield.weishu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaowei3 on 2016/8/25.
 */

/**
 * 要使用ButterKnife，就要重写一些方法
 */
public class AppBaseActivity extends SupportActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (onGetActivityLayout() != 0) {
            setContentView(onGetActivityLayout());
            ButterKnife.bind(this);
            onInitViewAndData(savedInstanceState);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 要写在onStart里，防止还没有setContentView
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(getToolbarTitle());
            mToolbar.setTitleTextAppearance(this, R.style.toolbar_text);
        }
    }

    protected int onGetActivityLayout() {
        return 0;
    }

    protected void onInitViewAndData(Bundle savedInstanceState) {

    }

    protected int getToolbarTitle() {
        return R.string.app_name;
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

}
