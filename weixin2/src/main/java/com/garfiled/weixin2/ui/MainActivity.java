package com.garfiled.weixin2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.garfield.baselib.fragmentation.SupportActivity;
import com.garfield.baselib.fragmentation.SupportFragment;
import com.garfiled.weixin2.R;
import com.garfiled.weixin2.base.AppBaseActivity;
import com.garfiled.weixin2.test.SwipeBackTestActivity;

/**
 * Created by gaowei3 on 2016/7/31.
 */
public class MainActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 旋转时会非空
        if (savedInstanceState == null) {
            loadRootFragment(R.id.main_activity_fragment_container, (SupportFragment) Fragment.instantiate(this, MainFragment.class.getName()));
        }
        //startActivity(new Intent(this, SwipeBackTestActivity.class));
    }
}
