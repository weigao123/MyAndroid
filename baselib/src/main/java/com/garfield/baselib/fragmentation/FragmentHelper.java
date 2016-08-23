package com.garfield.baselib.fragmentation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by gaowei3 on 2016/7/22.
 */
public class FragmentHelper {

    public static final String FRAGMENT_ARG_CONTAINER = "fragment_arg_container";

    void loadRootFragment(FragmentManager fragmentManager, int containerId, SupportFragment to) {
        bindContainerId(containerId, to);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        String toClassName = to.getClass().getName();
        ft.add(containerId, to, toClassName);
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
        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        String toClassName = to.getClass().getName();
        bindContainerId(from.getContainerId(), to);
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

    SupportFragment getTopFragment(FragmentManager fragmentManager) {
        List<Fragment> fragmentList = fragmentManager.getFragments();
        if (fragmentList == null) return null;

        for (int i = fragmentList.size() - 1; i >= 0; i--) {
            Fragment fragment = fragmentList.get(i);
            if (fragment instanceof SupportFragment) {
                return (SupportFragment) fragment;
            }
        }
        return null;
    }

    private void bindContainerId(int containerId, SupportFragment to) {
        Bundle args = to.getArguments();
        if (args == null) {
            args = new Bundle();
            to.setArguments(args);
        }
        args.putInt(FRAGMENT_ARG_CONTAINER, containerId);
    }

    SupportFragment findStackFragment(Class fragmentClass, FragmentManager fragmentManager) {
        Fragment fragment;
        fragment = fragmentManager.findFragmentByTag(fragmentClass.getName());
        return (SupportFragment) fragment;
    }

}
