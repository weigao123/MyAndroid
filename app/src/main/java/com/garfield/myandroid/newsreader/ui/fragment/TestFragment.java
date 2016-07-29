package com.garfield.myandroid.newsreader.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.myandroid.R;

/**
 * Created by gaowei3 on 2016/7/27.
 */
public class TestFragment extends BaseFragment {
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    protected void initView() {

    }
}
