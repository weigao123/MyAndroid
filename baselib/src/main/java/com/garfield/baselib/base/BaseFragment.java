package com.garfield.baselib.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.baselib.fragmentation.SupportActivity;
import com.garfield.baselib.fragmentation.SupportFragment;

/**
 * Created by gaowei3 on 2016/7/31.
 */
public abstract class BaseFragment extends Fragment {

    protected BaseActivity mActivity;

    @Override
    public void onAttach(Context context) {
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
