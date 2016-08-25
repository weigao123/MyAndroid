package com.garfiled.weixin2.test;

import android.os.Bundle;

import com.garfield.baselib.swipeback.SwipeBackActivity;
import com.garfiled.weixin2.R;

/**
 * Created by gaowei3 on 2016/8/25.
 */
public class SwipeBackTestActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting);
    }
}
