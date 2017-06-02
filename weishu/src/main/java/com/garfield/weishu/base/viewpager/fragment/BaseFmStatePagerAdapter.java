package com.garfield.weishu.base.viewpager.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaowei on 2017/5/27.
 */

public class BaseFmStatePagerAdapter extends FragmentStatePagerAdapter {

    protected List<Fragment> mItems;
    protected List<String> mTitles;
    protected boolean mIsInfinite;

    public BaseFmStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public BaseFmStatePagerAdapter(FragmentManager fm, List<Fragment> items) {
        super(fm);
        mItems = items;
    }

    public BaseFmStatePagerAdapter(FragmentManager fm, List<Fragment> items, List<String> titles) {
        super(fm);
        mItems = items;
        mTitles = titles;
    }

    public void addFragment(Fragment fragment) {
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        mItems.add(fragment);
    }

    public void addFragment(Fragment fragment, String title) {
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        if (mTitles == null) {
            mTitles = new ArrayList<>();
        }
        mItems.add(fragment);
        mTitles.add(title);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = getRealPosition(position);
        return super.instantiateItem(container, realPosition);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int realPosition = getRealPosition(position);
        super.destroyItem(container, realPosition, object);
    }

    @Override
    public Fragment getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mIsInfinite ? Integer.MAX_VALUE : mItems.size();
    }

    public CharSequence getPageTitle(int position) {
        return mTitles == null ? "" : mTitles.get(position);
    }

    /**
     * 必须有4个或以上个数的Page
     */
    public void setInfinite(boolean infinite) {
        mIsInfinite = infinite;
    }

    public int getRealPosition(int position) {
        return mIsInfinite ? position % mItems.size() : position;
    }
}
