package com.garfield.multifragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

/**
 * Created by gaowei3 on 2016/7/22.
 */
public class FragmentHelper {

    public static final String FRAGMENT_ARG_CONTAINER = "fragment_arg_container";

    public void loadRootFragment(FragmentManager fragmentManager, int containerId, SupportFragment to) {
        bindContainerId(containerId, to);
    }
    void startFragment(FragmentManager manager, SupportFragment from, SupportFragment to) {

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
