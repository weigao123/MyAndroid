package com.garfiled.weixin2.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.baselib.swipeback.fragment.SwipeBackFragment;
import com.garfiled.weixin2.R;
import com.garfiled.weixin2.base.AppBaseFragment;

/**
 * Created by gaowei3 on 2016/8/4.
 */
public class MsgFragment extends SwipeBackFragment {
    private Toolbar mToolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);
        initView(view);
        return attachToSwipeBack(view);
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("微信");
        mToolbar.setTitleTextAppearance(mActivity, R.style.toolbar_text);
        mToolbar.inflateMenu(R.menu.fragment_msg_list);
    }
}
