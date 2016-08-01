package com.garfiled.weixin2.ui;

import android.os.Bundle;

import com.garfield.baselib.base.BaseActivity;
import com.garfiled.weixin2.R;

/**
 * Created by gaowei3 on 2016/7/31.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startRootFragment(R.id.main_activity_fragment_container, MainFragment.class);
    }
}
