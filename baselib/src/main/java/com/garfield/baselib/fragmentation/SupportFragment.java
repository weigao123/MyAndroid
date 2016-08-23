package com.garfield.baselib.fragmentation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.garfield.baselib.base.BaseFragment;
import com.garfield.baselib.fragmentation.anim.DefaultHorizontalAnimator;
import com.garfield.baselib.fragmentation.anim.DefaultNoAnimator;
import com.garfield.baselib.fragmentation.anim.FragmentAnimator;

/**
 * Created by gaowei3 on 2016/7/22.
 */
public class SupportFragment extends BaseFragment implements ISupport {

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    protected SupportActivity mActivity;

    private FragmentAnimator mFragmentAnimator;

    private int mContainerId;   // 该Fragment所处的Container的id

    private FragmentHelper mFragmentHelper;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SupportActivity) {
            this.mActivity = (SupportActivity) context;
            mFragmentHelper = mActivity.getFragmentHelper();
        } else {
            throw new RuntimeException("Must extends SupportActivity!");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentAnimator = onCreateFragmentAnimator();
        if (mFragmentAnimator == null) {
            mFragmentAnimator = mActivity.getFragmentAnimator();
            if (mFragmentAnimator == null) {
                mFragmentAnimator = new DefaultNoAnimator();
            }
        }
        Bundle bundle = getArguments();
        mContainerId = bundle.getInt(FragmentHelper.FRAGMENT_ARG_CONTAINER);

        if (savedInstanceState != null) {
            boolean isFragmentHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isFragmentHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return AnimationUtils.loadAnimation(mActivity, mFragmentAnimator.getEnter());
        } else {
            return AnimationUtils.loadAnimation(mActivity, mFragmentAnimator.getExit());
        }
    }

    protected FragmentAnimator onCreateFragmentAnimator() {
        return null;
    }

    public int getContainerId() {
        return mContainerId;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Override
    public void loadRootFragment(int containerId, SupportFragment toFragment) {
        mFragmentHelper.loadRootFragment(getChildFragmentManager(), containerId, toFragment);
    }

    @Override
    public void replaceLoadRootFragment(int containerId, SupportFragment toFragment, boolean addToBack) {

    }

    @Override
    public void loadMultiRootFragment(int containerId, int showPosition, SupportFragment... toFragments) {
        mFragmentHelper.loadMultiRootFragment(getFragmentManager(), containerId, showPosition, toFragments);
    }

    @Override
    public void showHideFragment(SupportFragment showFragment, SupportFragment hideFragment) {
        mFragmentHelper.showHideFragment(getFragmentManager(), showFragment, hideFragment);
    }

    @Override
    public void startFragment(SupportFragment toFragment) {
        mFragmentHelper.startFragment(getFragmentManager(), getTopFragment(), toFragment);
    }

    @Override
    public void startFragment(SupportFragment toFragment, int launchMode) {

    }

    @Override
    public void startFragmentWithPop(SupportFragment toFragment) {
        mFragmentHelper.startFragmentWithPop(getFragmentManager(), getTopFragment(), toFragment);
    }

    @Override
    public void startFragmentForResult(SupportFragment toFragment, int requestCode) {

    }

    @Override
    public SupportFragment getTopFragment() {
        return mFragmentHelper.getTopFragment(getFragmentManager());
    }

    @Override
    public void popFragment() {

    }

    @Override
    public void popToFragment(Class<?> fragmentClass, boolean includeSelf) {

    }

    @Override
    public void popToFragment(Class<?> fragmentClass, boolean includeSelf, Runnable afterPopTransactionRunnable) {

    }

    public SupportFragment findFragment(Class fragmentClass) {
        return mFragmentHelper.findStackFragment(fragmentClass, getFragmentManager());
    }


}
