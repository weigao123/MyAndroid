package com.garfield.weishu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.garfield.baselib.fragmentation.SupportActivity;
import com.garfield.weishu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaowei3 on 2016/8/25.
 */
public class AppBaseActivity extends SupportActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    private Toolbar mToolbar;

    @Override
    protected void onStart() {
        super.onStart();
        ButterKnife.bind(this);

        if (mToolbar != null) {
            mToolbar.setTitle(getToolbarTitle());
            mToolbar.setTitleTextAppearance(this, R.style.toolbar_text);
            //mToolbar.inflateMenu(R.menu.fragment_msg_list);
            //mToolbar.setOnMenuItemClickListener(this);

            ImageView addView = (ImageView) findViewById(R.id.toolbar_add_view);
            addView.setRotation(45);
            //addView.setColorFilter(ContextCompat.getColor(mActivity, R.color.white));
            findViewById(R.id.toolbar_add).setOnClickListener(this);
            findViewById(R.id.toolbar_search).setOnClickListener(this);
        }
    }

    protected int getToolbarTitle() {
        return R.string.app_name;
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void onClick(View v) {

    }
}
