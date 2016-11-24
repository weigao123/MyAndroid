package com.garfield.baselib.fragmentation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentTransactionBugFixHack;

import java.util.List;

/**
 * Created by gaowei3 on 2016/7/22.
 */
public class FragmentHelper {

    public static final String FRAGMENT_ARG_CONTAINER = "fragment_arg_container";
    static final String ARG_RESULT_RECORD = "fragment_arg_result_record";
    private int rootId;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private SupportActivity mActivity;

    public FragmentHelper(SupportActivity activity) {
        mActivity = activity;
    }

    void loadRootFragment(FragmentManager fragmentManager, int containerId, SupportFragment to) {
        mActivity.onSwitchToFragment(to);
        bindContainerId(containerId, to);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //根Fragment不能设置动画
        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        String toClassName = to.getClass().getName();
        ft.add(containerId, to, toClassName);
        ft.addToBackStack(toClassName);
        ft.commit();
    }

    void loadMultiRootFragment(FragmentManager fragmentManager, int containerId, int showPosition, SupportFragment... tos) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        for (int i = 0; i < tos.length; i++) {
            SupportFragment to = tos[i];
            bindContainerId(containerId, to);
            String toName = to.getClass().getName();
            ft.add(containerId, to, toName);
            if (i != showPosition) {
                ft.hide(to);
            }
        }
        ft.commit();
    }

    void showHideFragment(FragmentManager fragmentManager, SupportFragment showFragment, SupportFragment hideFragment) {
        if (showFragment == hideFragment) return;
        fragmentManager.beginTransaction().show(showFragment).hide(hideFragment).commit();
    }

    /**
     * 前一个hide掉，from不能是null
     */
    void startFragment(FragmentManager fragmentManager, SupportFragment from, SupportFragment to) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        String toClassName = to.getClass().getName();
        bindContainerId(from.getContainerId(), to);
        mActivity.onSwitchToFragment(to);
        ft.add(from.getContainerId(), to, toClassName);
        ft.hide(from);
        ft.addToBackStack(toClassName);
        ft.commit();
    }

    /**
     * 前一个remove掉，from不能是null
     */
    void startFragmentWithPop(FragmentManager fragmentManager, SupportFragment from, SupportFragment to) {
        fragmentManager.beginTransaction().remove(from).commit();

        String toClassName = to.getClass().getName();
        FragmentTransaction ft = fragmentManager.beginTransaction()
                //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(from.getContainerId(), to, toClassName)
                .addToBackStack(toClassName);

        ft.commit();
    }

    private void bindContainerId(int containerId, SupportFragment to) {
        Bundle args = to.getArguments();
        if (args == null) {
            args = new Bundle();
            to.setArguments(args);
        }
        args.putInt(FRAGMENT_ARG_CONTAINER, containerId);
    }

    SupportFragment findStackFragment(FragmentManager fragmentManager, Class fragmentClass) {
        Fragment fragment;
        fragment = fragmentManager.findFragmentByTag(fragmentClass.getName());
        return (SupportFragment) fragment;
    }

    /**
     * 返回到上一个
     */
    void popBack(FragmentManager fragmentManager) {
        SupportFragment targetFragment = getPreOfTopFragment(fragmentManager);
        mActivity.onSwitchToFragment(targetFragment);
        fragmentManager.popBackStackImmediate();
        if (targetFragment != null && targetFragment.getFragmentResult() != null) {
            targetFragment.onFragmentResult(targetFragment.getFragmentResult());
        }
    }

    /**
     * 返回到特定的某一个
     */
    void popToFragment(final FragmentManager fragmentManager, Class<?> fragmentClass, boolean includeSelf) {
        int flag;
        SupportFragment thisFragment = getTopFragment(fragmentManager);
        SupportFragment targetFragment = (SupportFragment) fragmentManager.findFragmentByTag(fragmentClass.getName());
        mActivity.onSwitchToFragment(targetFragment);
        if (false) {
            //暂时不用
            flag = FragmentManager.POP_BACK_STACK_INCLUSIVE;
            targetFragment = getPreFragment(targetFragment);
        } else {
            flag = 0;
        }
        fragmentManager.popBackStackImmediate(fragmentClass.getName(), flag);
        if (targetFragment != null && thisFragment != null && thisFragment.getFragmentResult() != null) {
            targetFragment.onFragmentResult(thisFragment.getFragmentResult());
        }
    }

    SupportFragment getTopFragment(FragmentManager fragmentManager) {
        FragmentTransactionBugFixHack.reorderIndices(fragmentManager);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        if (fragmentList == null) return null;
        /**
         * 有时候size=2，但是只有一个value，这时fragment可以为null，会跳过
         */
        for (int i = fragmentList.size() - 1; i >= 0; i--) {
            Fragment fragment = fragmentList.get(i);
            if (fragment instanceof SupportFragment) {
                //L.d(fragment.getClass().getSimpleName());
                return (SupportFragment) fragment;
            }
        }
        return null;
    }

    SupportFragment getPreOfTopFragment(FragmentManager fragmentManager) {
        return getPreFragment(getTopFragment(fragmentManager));
    }

    private SupportFragment getPreFragment(Fragment fragment) {
        if (fragment == null) return null;
        //调用getPre时，getTop都已经执行过了
        //FragmentTransactionBugFixHack.reorderIndices(fragment.getFragmentManager());
        List<Fragment> fragmentList = fragment.getFragmentManager().getFragments();
        if (fragmentList == null) return null;

        int index = fragmentList.indexOf(fragment);
        for (int i = index - 1; i >= 0; i--) {
            Fragment preFragment = fragmentList.get(i);
            if (preFragment instanceof SupportFragment) {
                return (SupportFragment) preFragment;
            }
        }
        return null;
    }

    Handler getHandler() {
        return mHandler;
    }
}
