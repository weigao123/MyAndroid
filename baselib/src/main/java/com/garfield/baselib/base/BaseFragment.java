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
    protected View mRootView;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (onGetFragmentLayout() != 0) {
            mRootView = inflater.inflate(onGetFragmentLayout(), container, false);
            onInitView(mRootView);
        }
        return mRootView;
    }

    protected void onInitView(View view) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected int onGetFragmentLayout() {
        return 0;
    }
}
