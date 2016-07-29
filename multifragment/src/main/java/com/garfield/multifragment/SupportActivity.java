package com.garfield.multifragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.garfield.multifragment.anim.FragmentAnimator;

/**
 * Created by gaowei3 on 2016/7/22.
 */
public class SupportActivity extends AppCompatActivity {

    private FragmentAnimator mFragmentAnimator;
    private FragmentHelper mFragmentHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentAnimator = onCreateFragmentAnimator();
        mFragmentHelper = new FragmentHelper();
    }

    protected FragmentAnimator onCreateFragmentAnimator() {
        return null;
    }

    public FragmentAnimator getFragmentAnimator() {
        return mFragmentAnimator;
    }

    /**
     * Root
     */
    public void startRootFragment(int containerId, Class toClass) {
        startRootFragment(containerId, toClass, null);
    }
    public void startRootFragment(int containerId, Class toClass, Bundle bundle) {
        Fragment to = Fragment.instantiate(getApplicationContext(), toClass.getName(), bundle);
        if (to instanceof SupportFragment) {
            startRootFragment(containerId, (SupportFragment)to);
        }
    }
    public void startRootFragment(int containerId, SupportFragment to) {
        mFragmentHelper.startRootFragment(getSupportFragmentManager(), containerId, to);
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
