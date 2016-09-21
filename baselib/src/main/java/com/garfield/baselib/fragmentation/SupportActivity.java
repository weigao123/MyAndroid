package com.garfield.baselib.fragmentation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.garfield.baselib.base.BaseActivity;
import com.garfield.baselib.fragmentation.anim.FragmentAnimator;
import com.garfield.baselib.swipeback.SwipeBackActivity;

import java.util.List;

/**
 * Created by gaowei3 on 2016/7/22.
 */
public class SupportActivity extends SwipeBackActivity implements ISupport {

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

    /********  以下全都是加载第一级Fragment  **********/

    @Override
    public void loadRootFragment(int containerId, SupportFragment to) {
        mFragmentHelper.loadRootFragment(getSupportFragmentManager(), containerId, to);
    }

    @Override
    public void replaceLoadRootFragment(int containerId, SupportFragment toFragment, boolean addToBack) {

    }

    @Override
    public void loadMultiRootFragment(int containerId, int showPosition, SupportFragment... toFragments) {
        mFragmentHelper.loadMultiRootFragment(getSupportFragmentManager(), containerId, showPosition, toFragments);
    }

    public void showHideFragment(SupportFragment showFragment, SupportFragment hideFragment) {
        mFragmentHelper.showHideFragment(getSupportFragmentManager(), showFragment, hideFragment);
    }

    @Override
    public void startFragment(SupportFragment toFragment) {
        mFragmentHelper.startFragment(getSupportFragmentManager(), getTopFragment(), toFragment);
    }

    @Override
    public void startFragment(SupportFragment toFragment, int launchMode) {

    }

    /**
     * start with remove
     */
    @Override
    public void startFragmentWithPop(SupportFragment toFragment) {
        mFragmentHelper.startFragmentWithPop(getSupportFragmentManager(), getTopFragment(), toFragment);
    }

    @Override
    public void startFragmentForResult(SupportFragment toFragment, int requestCode) {

    }


    @Override
    public void popFragment() {

    }

    @Override
    public void popChildFragment() {

    }

    @Override
    public void popToFragment(Class<?> fragmentClass, boolean includeSelf) {

    }

    @Override
    public void popToFragment(Class<?> fragmentClass, boolean includeSelf, Runnable afterPopTransactionRunnable) {

    }





    @Override
    public SupportFragment getTopFragment() {
        return mFragmentHelper.getTopFragment(getSupportFragmentManager());
    }

    @Override
    public <T extends SupportFragment> T findFragment(Class<T> fragmentClass) {
        return null;
    }

    public void procBackPressed(boolean isToBack) {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            super.onBackPressed();
        } else {
            if (isToBack)
                moveTaskToBack(false);
            else
                finish();
        }
    }
}
