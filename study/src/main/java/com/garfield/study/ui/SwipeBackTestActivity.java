package com.garfield.study.ui;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.garfield.baselib.fragmentation.SupportActivity;
import com.garfield.study.R;

/**
 * Created by gaowei3 on 2016/8/25.
 */
public class SwipeBackTestActivity extends SupportActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        //attachToSwipeBack();
        //setSwipeBackEnable(true);
    }
}
