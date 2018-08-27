package com.garfield.baselib.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.baselib.utils.system.L;

/**
 * Created by gaowei3 on 2016/8/2.
 */
public class MiddleFragment extends BaseFragment {

    private final static String TAG = MiddleFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        L.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        L.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        L.d(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L.d(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        L.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        L.d(TAG, "onResume");
    }

}
