package com.garfiled.weixin2.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.baselib.fragmentation.SupportFragment;
import com.garfiled.weixin2.R;
import com.garfiled.weixin2.base.AppBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaowei3 on 2016/8/1.
 */
public class ContactFragment extends AppBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

    }



}
