package com.garfield.weishu.base.viewpager.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.garfield.baselib.utils.system.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaowei on 2017/6/6.
 */

public class InfiniteFmAdapter extends FragmentPagerAdapter {

    protected List<Fragment> mItems;
    private FragmentManager mFragmentManager;

    public InfiniteFmAdapter(FragmentManager fm) {
        this(fm, null);
    }

    public InfiniteFmAdapter(FragmentManager fm, List<Fragment> items) {
        super(fm);
        mFragmentManager = fm;
        mItems = items;
    }

    public void addFragment(Fragment fragment) {
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        mItems.add(fragment);
    }

    public void changeFragments(List<Fragment> newItems) {
        if (mItems != null) {
            FragmentTransaction curTransaction = mFragmentManager.beginTransaction();
            for (Fragment f : mItems) {
                curTransaction.remove(f);
            }
            curTransaction.commit();
            mFragmentManager.executePendingTransactions();
        }
        mItems = newItems;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 规避setAdapter后又setCurrentItem，重复执行本方法，而只能add同一个fragment一次
        // 避开此问题
        if (position < 10) {
            return new Fragment();
        }
        int realPosition = getRealPosition(position);
        L.d("instantiateItem real:"+realPosition);
        return super.instantiateItem(container, realPosition);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int realPosition = getRealPosition(position);
        L.d("destroyItem real:"+realPosition);
        super.destroyItem(container, realPosition, object);
    }

    @Override
    public Fragment getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : Integer.MAX_VALUE;
    }

    /**
     * setOffscreenPageLimit
     * 1 不能全部
     * 2 如果是默认1，必须有4个或以上个数的Page
     * 3 如果是2，必须有6个或以上的Page
     */
    public int getRealPosition(int position) {
        return (mItems.size() != 0) ? position % mItems.size() : position;
    }

    public List<Fragment> getItems() {
        return mItems;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
