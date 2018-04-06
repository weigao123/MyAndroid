package com.garfield.study.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.garfield.baselib.swipeback.SwipeBackActivity;
import com.garfield.baselib.utils.system.L;
import com.garfield.study.R;
import com.garfield.study.provider.ProviderFragment;

/**
 * Created by gaowei on 2017/7/10.
 */

public class SingleFragmentActivity extends SwipeBackActivity {

    //protected abstract Fragment createFragment();

    protected int getLayoutResId() {
        return R.layout.activity_single_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        L.d("SingleFragmentActivity");
        if (fragment == null) {
            fragment = new ProviderFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }

        attachToSwipeBack();
        setSwipeBackEnable(true);
    }


}
