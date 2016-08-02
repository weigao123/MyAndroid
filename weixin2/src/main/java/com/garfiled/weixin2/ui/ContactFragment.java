package com.garfiled.weixin2.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.baselib.base.BaseFragment;
import com.garfield.baselib.fragmentation.SupportFragment;
import com.garfiled.weixin2.R;

/**
 * Created by gaowei3 on 2016/8/1.
 */
public class ContactFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        return view;
    }


}
