package com.garfiled.weixin2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.baselib.base.BaseFragment;
import com.garfield.baselib.widget.BottomBar;
import com.garfield.baselib.widget.BottomBarTab;
import com.garfiled.weixin2.R;

/**
 * Created by gaowei3 on 2016/7/31.
 */
public class MainFragment extends BaseFragment implements View.OnClickListener {

    private BottomBar mBottomBar;

    @Override
    protected int createView() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View view) {
        mBottomBar = (BottomBar) view.findViewById(R.id.bottomBar);
        mBottomBar
                .addItem(new BottomBarTab(mActivity, R.drawable.ic_message_white, "消息"))
                .addItem(new BottomBarTab(mActivity, R.drawable.ic_contact_white, "联系人"))
                .addItem(new BottomBarTab(mActivity, R.drawable.ic_discover_white, "发现"));
        this.getView().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mBottomBar.isVisible()) {
            mBottomBar.hide();
        } else {
            mBottomBar.show();
        }
    }
}
