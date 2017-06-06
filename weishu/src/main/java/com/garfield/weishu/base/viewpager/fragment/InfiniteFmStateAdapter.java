package com.garfield.weishu.base.viewpager.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by gaowei on 2017/6/6.
 */

public abstract class InfiniteFmStateAdapter extends FragmentStatePagerAdapter {

    public InfiniteFmStateAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return getRealItem(position % getRealCount());
    }

    public abstract Fragment getRealItem(int position);

    public abstract int getRealCount();

    @Override
    public int getCount() {
        return getRealCount() == 0 ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public int getItemPosition(Object object) {
        //return POSITION_UNCHANGED;
        return POSITION_NONE;
    }
}