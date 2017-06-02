package com.garfield.weishu.base.viewpager.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaowei on 2017/5/27.
 */

public class BaseFmPagerAdapter extends FragmentPagerAdapter {

    protected List<Fragment> mItems;
    protected List<String> mTitles;
    protected boolean mIsInfinite;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;

    public BaseFmPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    public BaseFmPagerAdapter(FragmentManager fm, List<Fragment> items) {
        super(fm);
        mItems = items;
        mFragmentManager = fm;
    }

    public BaseFmPagerAdapter(FragmentManager fm, List<Fragment> items, List<String> titles) {
        super(fm);
        mItems = items;
        mTitles = titles;
        mFragmentManager = fm;
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
        Fragment fragment = (Fragment) super.instantiateItem(container, realPosition);

//        if (mCurTransaction == null) {
//            mCurTransaction = mFragmentManager.beginTransaction();
//        }
//        mCurTransaction.remove(fragment);
//        mCurTransaction.add(container.getId(), fragment, fragment.getTag());
//        mCurTransaction.commit();
        return fragment;
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
     * 必须有3个或以上个数的Page
     */
    public void setInfinite(boolean infinite) {
        mIsInfinite = infinite;
    }

    public int getRealPosition(int position) {
        return mIsInfinite ? position % mItems.size() : position;
    }
}
