package com.garfield.baselib.fragmentation;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.garfield.baselib.R;
import com.garfield.baselib.base.BaseFragment;
import com.garfield.baselib.fragmentation.anim.DefaultNoAnimator;
import com.garfield.baselib.fragmentation.anim.FragmentAnimator;
import com.garfield.baselib.utils.system.KeyboardUtil;

/**
 * Created by gaowei3 on 2016/7/22.
 */
public class SupportFragment extends BaseFragment implements ISupport {

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    protected SupportActivity mActivity;

    private FragmentAnimator mFragmentAnimator;

    private int mContainerId;   // 该Fragment所处的Container的id

    private FragmentHelper mFragmentHelper;

    private boolean isAnimationEnable = true;

    private Bundle mResultBundle;

    protected final Handler getHandler() {
        return mFragmentHelper.getHandler();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SupportActivity) {
            mFragmentAnimator = onCreateFragmentAnimator();
            mActivity = (SupportActivity) context;
            mFragmentHelper = mActivity.getFragmentHelper();
            if (mFragmentAnimator == null) {
                mFragmentAnimator = mActivity.getFragmentAnimator();
                if (mFragmentAnimator == null) {
                    mFragmentAnimator = new DefaultNoAnimator();
                }
            }
        } else {
            throw new RuntimeException("Must extends SupportActivity!");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        // NewsTabFragment是由adapter启动，这类较为特殊，没有bundle
        if (bundle != null) {
            mContainerId = bundle.getInt(FragmentHelper.FRAGMENT_ARG_CONTAINER);
        }

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
        if (isFragmentSwipeBacking()) {
            return AnimationUtils.loadAnimation(mActivity, R.anim.no_anim);
        }
        if (transit == FragmentTransaction.TRANSIT_FRAGMENT_OPEN) {
            // 打开时，两个都是TRANSIT_FRAGMENT_OPEN
            if (enter) {
                return AnimationUtils.loadAnimation(mActivity, mFragmentAnimator.getEnter());
            } else {
                return AnimationUtils.loadAnimation(mActivity, mFragmentAnimator.getExit());
            }
        } else if (transit == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE) {
            // 返回键时，两个都是TRANSIT_FRAGMENT_CLOSE
            if (enter) {
                return AnimationUtils.loadAnimation(mActivity, mFragmentAnimator.getPopEnter());
            } else {
                return AnimationUtils.loadAnimation(mActivity, mFragmentAnimator.getPopExit());
            }
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    /**
     * 打开时，有时候会卡顿，可以把打开的动画去掉
     */
    protected void setAnimationEnable(boolean enable) {
        isAnimationEnable = enable;
    }

    public boolean isAnimationEnable() {
        return isAnimationEnable;
    }

    protected FragmentAnimator onCreateFragmentAnimator() {
        return null;
    }

    public int getContainerId() {
        return mContainerId;
    }

    /**
     * 1、正在pop back时，返回true，用于滑动关闭时无动画
     * 2、执行popToFragment时，关闭动画
     */
    protected boolean isFragmentSwipeBacking() {
        return false;
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
        // 这里必须使用getChildFragmentManager，才能使用getFragments()时不会把这些列出来
        mFragmentHelper.loadMultiRootFragment(getChildFragmentManager(), containerId, showPosition, toFragments);
    }

    @Override
    public void showHideFragment(SupportFragment showFragment, SupportFragment hideFragment) {
        mFragmentHelper.showHideFragment(getChildFragmentManager(), showFragment, hideFragment);
    }

    /********  以上都是加载嵌套子Fragment，以下都是加载同级Fragment  **********/

    @Override
    public void startFragment(SupportFragment toFragment) {
        mFragmentHelper.startFragment(getFragmentManager(), this, toFragment);
    }

    @Override
    public void startFragment(SupportFragment toFragment, int launchMode) {

    }

    @Override
    public void startFragmentWithPop(SupportFragment toFragment) {
        mFragmentHelper.startFragmentWithPop(getFragmentManager(), this, toFragment);
    }

    @Override
    public void startFragmentForResult(SupportFragment toFragment, int requestCode) {

    }

    @Override
    public SupportFragment getTopFragment() {
        return mFragmentHelper.getTopFragment(getFragmentManager());
    }

    @Override
    public SupportFragment getPreOfTopFragment() {
        return mFragmentHelper.getPreOfTopFragment(getFragmentManager());
    }

    @Override
    public void popFragment() {
        KeyboardUtil.hideKeyboard(getView());
        mFragmentHelper.popBack(getFragmentManager());
    }

    @Override
    public void popChildFragment() {
        KeyboardUtil.hideKeyboard(getView());
        mFragmentHelper.popBack(getChildFragmentManager());
    }

    @Override
    public void popToFragment(Class<?> fragmentClass, boolean includeSelf) {
        mFragmentHelper.popToFragment(getFragmentManager(), fragmentClass, includeSelf);
    }

    @Override
    public void popToFragment(Class<?> fragmentClass, boolean includeSelf, Runnable afterPopTransactionRunnable) {

    }

    @Override
    public <T extends SupportFragment> T findFragment(Class<T> fragmentClass) {
        return (T) mFragmentHelper.findStackFragment(getFragmentManager(), fragmentClass);
    }

    protected void setFragmentResult(Bundle bundle) {
        mResultBundle = bundle;
    }

    Bundle getFragmentResult() {
        return mResultBundle;
    }

    protected void onFragmentResult(Bundle data) {
    }

    protected boolean onBackPressed() {
        return false;
    }
}
