package com.garfield.study.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.garfield.baselib.BuildConfig;
import com.garfield.baselib.swipeback.SwipeBackActivity;
import com.garfield.baselib.utils.system.L;
import com.garfield.study.R;
import com.garfield.study.screenshot.ScreenshotFragment;
import com.garfield.study.task.TaskFragment;

import java.util.HashMap;
import java.util.Map;

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
            fragment = new ScreenshotFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }

        attachToSwipeBack();
        setSwipeBackEnable(true);
        L.d("gaowei study", BuildConfig.BUILD_TYPE);


        HashMap<Integer, String> map1 = new HashMap<Integer, String>();
        map1.put(1, "name");
        HashMap<Integer, String> map2 = new HashMap<>();
        map2.put(1, "name");
        Map<Integer, String> map3 = new HashMap<>();
        map3.put(1, "name");
    }


}
