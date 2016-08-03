package com.garfiled.weixin2.ui;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.baselib.base.BaseFragment;
import com.garfiled.weixin2.R;

/**
 * Created by gaowei3 on 2016/8/1.
 */
public class MsgListFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener {

    private Toolbar mToolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg_list, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("消息");
        mToolbar.inflateMenu(R.menu.fragment_msg_list);
        mToolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_list:
                final PopupMenu popupMenu = new PopupMenu(mActivity, mToolbar, Gravity.END);
                popupMenu.inflate(R.menu.fragment_msg_list);
                popupMenu.show();
        }
        return false;
    }
}
