package com.garfield.baselib.base;

import android.os.Bundle;

import com.garfield.baselib.utils.system.L;

/**
 * Created by gaowei3 on 2016/8/2.
 */
public class MiddleActivity extends BaseActivity {

    private final static String TAG = MiddleActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        L.d(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onStart() {
        super.onStart();
        L.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.d(TAG, "onResume");
    }
}
