package com.gaowei.myandroid.newsreader.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gaowei3 on 2016/7/18.
 */
public abstract class BaseFragment extends Fragment {

    protected abstract View createView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState);

    protected abstract void initView();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return createView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }
}
