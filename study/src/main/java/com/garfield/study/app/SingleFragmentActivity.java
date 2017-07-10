package com.garfield.study.app;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.garfield.baselib.swipeback.SwipeBackActivity;
import com.garfield.study.R;

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

        if (fragment == null) {
            fragment = new AppBaseFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        attachToSwipeBack();
        setSwipeBackEnable(true);
    }


}
