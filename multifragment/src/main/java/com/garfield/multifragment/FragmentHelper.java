package com.garfield.multifragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import java.util.List;

/**
 * Created by gaowei3 on 2016/7/22.
 */
public class FragmentHelper {

    public static final String FRAGMENT_ARG_CONTAINER = "fragment_arg_container";

    public void startRootFragment(FragmentManager fragmentManager, int containerId, SupportFragment to) {
        bindContainerId(containerId, to);
        startFragment(fragmentManager, null, to);
    }

    /**
     * start with hide.
     */
    public void startFragment(FragmentManager fragmentManager, SupportFragment from, SupportFragment to) {
        String toClassName = to.getClass().getName();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        if (from == null) {
            // the arg has been put into fragment.
            ft.add(to.getArguments().getInt(FRAGMENT_ARG_CONTAINER), to, toClassName);
        } else {
            bindContainerId(from.getContainerId(), to);
            ft.add(from.getContainerId(), to, toClassName);
            ft.hide(from);
        }

        ft.addToBackStack(toClassName);
        ft.commit();
    }

    /**
     * start with remove
     */
    public void startFragmentWithPop(FragmentManager fragmentManager, SupportFragment from, SupportFragment to) {
        fragmentManager.beginTransaction().remove(from).commit();

        String toName = to.getClass().getName();
        FragmentTransaction ft = fragmentManager.beginTransaction()
                //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(from.getContainerId(), to, toName)
                .addToBackStack(toName);

        ft.commit();
    }

    public SupportFragment getTopFragment(FragmentManager fragmentManager) {
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


}
