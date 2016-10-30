package com.garfield.baselib.fragmentation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.garfield.baselib.base.BaseActivity;
import com.garfield.baselib.fragmentation.anim.FragmentAnimator;
import com.garfield.baselib.swipeback.SwipeBackActivity;
import com.garfield.baselib.utils.L;

import java.util.List;

/**
 * Created by gaowei3 on 2016/7/22.
 */
public class SupportActivity extends BaseActivity implements ISupport {

    private FragmentAnimator mFragmentAnimator;
    private FragmentHelper mFragmentHelper;

    private boolean mIsToBack;

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
            mFragmentHelper = new FragmentHelper(this);
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

    @Override
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
        mFragmentHelper.popBack(getSupportFragmentManager());
    }

    @Override
    public void popChildFragment() {

    }

    @Override
    public void popToFragment(Class<?> fragmentClass, boolean includeSelf) {
        mFragmentHelper.popToFragment(getSupportFragmentManager(), fragmentClass, includeSelf);
    }

    @Override
    public void popToFragment(Class<?> fragmentClass, boolean includeSelf, Runnable afterPopTransactionRunnable) {

    }

    @Override
    public SupportFragment getTopFragment() {
        return mFragmentHelper.getTopFragment(getSupportFragmentManager());
    }

    public SupportFragment getPreOfTopFragment() {
        return mFragmentHelper.getPreOfTopFragment(getSupportFragmentManager());
    }

    @Override
    public <T extends SupportFragment> T findFragment(Class<T> fragmentClass) {
        return (T) mFragmentHelper.findStackFragment(getSupportFragmentManager(), fragmentClass);
    }

    public void setIsToBack(boolean isToBack) {
        mIsToBack = isToBack;
    }

    /**
     * 只提供第一层级的fragment
     */
    protected void onSwitchToFragment(Fragment fragment) {
        if (fragment != null) {
            L.d("onSwitchFragment: "+ fragment.getClass().getSimpleName());
        } else {
            L.d("onSwitchFragment: null");
        }
    }

    @Override
    public void onBackPressed() {
        if (getTopFragment().onBackPressed()) {
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() >= 2) {
            //super.onBackPressed();
            popFragment();
        } else {
            onSwitchToFragment(null);
            if (mIsToBack)
                moveTaskToBack(false);
            else
                finish();
        }
    }
}
