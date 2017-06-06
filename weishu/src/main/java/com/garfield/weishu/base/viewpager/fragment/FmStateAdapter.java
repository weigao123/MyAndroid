package com.garfield.weishu.base.viewpager.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaowei on 2017/5/27.
 */

public class FmStateAdapter extends FragmentStatePagerAdapter {

    protected List<Fragment> mItems;
    protected List<String> mTitles;

    public FmStateAdapter(FragmentManager fm) {
        this(fm, null);
    }

    public FmStateAdapter(FragmentManager fm, List<Fragment> items) {
        this(fm, items, null);
    }

    public FmStateAdapter(FragmentManager fm, List<Fragment> items, List<String> titles) {
        super(fm);
        mItems = items;
        mTitles = titles;
    }

    public void addFragment(Fragment fragment) {
        addFragment(fragment, null);
    }

    public void addFragment(Fragment fragment, String title) {
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        mItems.add(fragment);
        if (title != null) {
            if (mTitles == null) {
                mTitles = new ArrayList<>();
            }
            mTitles.add(title);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public CharSequence getPageTitle(int position) {
        return mTitles == null ? "" : mTitles.get(position);
    }

    public List<Fragment> getItems() {
        return mItems;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
