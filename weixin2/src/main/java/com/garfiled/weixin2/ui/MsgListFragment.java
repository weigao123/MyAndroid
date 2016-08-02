package com.garfiled.weixin2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.baselib.base.BaseFragment;
import com.garfiled.weixin2.R;

/**
 * Created by gaowei3 on 2016/8/1.
 */
public class MsgListFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg_list, container, false);

        return view;
    }


}
