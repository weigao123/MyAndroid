package com.garfiled.weixin2.test;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.garfiled.weixin2.R;
import com.garfiled.weixin2.base.AppBaseActivity;

/**
 * Created by gaowei3 on 2016/8/25.
 */
public class SwipeBackTestActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        attachToSwipeBack();
        setSwipeBackEnable(true);
    }
}
