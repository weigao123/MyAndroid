package com.gaowei.myandroid.newsreader.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaowei.myandroid.R;

import java.util.ArrayList;


public class NewsFragment extends BaseFragment {

    private ViewPager mViewPager;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    protected void initView() {
        mViewPager = (ViewPager) getActivity().findViewById(R.id.newsreader_news_viewpager);
        mViewPager.setAdapter(mFragmentPagerAdapter);
    }

    private FragmentStatePagerAdapter mFragmentPagerAdapter = new FragmentStatePagerAdapter() {


        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }
    };
}
