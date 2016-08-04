package com.garfield.baselib.fragmentation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.garfield.baselib.base.BaseActivity;
import com.garfield.baselib.fragmentation.anim.FragmentAnimator;

/**
 * Created by gaowei3 on 2016/7/22.
 */
public class SupportActivity extends BaseActivity {

    private FragmentAnimator mFragmentAnimator;
    private FragmentHelper mFragmentHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentAnimator = onCreateFragmentAnimator();
        getFragmentHelper();
    }

    protected FragmentAnimator onCreateFragmentAnimator() {
        return null;
    }

    public FragmentAnimator getFragmentAnimator() {
        return mFragmentAnimator;
    }

    public FragmentHelper getFragmentHelper() {
        if (mFragmentHelper == null) {
            mFragmentHelper = new FragmentHelper();
        }
        return mFragmentHelper;
    }

    public void loadRootFragment(int containerId, SupportFragment to) {
        mFragmentHelper.loadRootFragment(getSupportFragmentManager(), containerId, to);
    }

    public void loadMultiRootFragment(int containerId, int showPosition, SupportFragment... toFragments) {
        mFragmentHelper.loadMultiRootFragment(getSupportFragmentManager(), containerId, showPosition, toFragments);
    }

    public void showHideFragment(SupportFragment showFragment, SupportFragment hideFragment) {
        mFragmentHelper.showHideFragment(getSupportFragmentManager(), showFragment, hideFragment);
    }
















    /**
     * start with hide
     */
    public void startFragment(Class toClass) {
        startFragment(toClass, null);
    }
    public void startFragment(Class toClass, Bundle bundle) {
        Fragment to = Fragment.instantiate(getApplicationContext(), toClass.getName(), bundle);
        if (to instanceof SupportFragment) {
            startFragment((SupportFragment)to);
        }
    }
    public void startFragment(SupportFragment to) {
        startFragment(mFragmentHelper.getTopFragment(getSupportFragmentManager()), to);
    }
    public void startFragment(SupportFragment from, SupportFragment to) {
        mFragmentHelper.startFragment(getSupportFragmentManager(), from, to);
    }

    /**
     * start with remove
     */
    public void startFragmentWithPop(Class toClass) {
        startFragmentWithPop(toClass, null);
    }
    public void startFragmentWithPop(Class toClass, Bundle bundle) {
        Fragment to = Fragment.instantiate(getApplicationContext(), toClass.getName(), bundle);
        if (to instanceof SupportFragment) {
            startFragmentWithPop((SupportFragment)to);
        }
    }
    public void startFragmentWithPop(SupportFragment to) {
        startFragmentWithPop(mFragmentHelper.getTopFragment(getSupportFragmentManager()), to);
    }
    public void startFragmentWithPop(SupportFragment from, SupportFragment to) {
        mFragmentHelper.startFragmentWithPop(getSupportFragmentManager(), from, to);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }
}
