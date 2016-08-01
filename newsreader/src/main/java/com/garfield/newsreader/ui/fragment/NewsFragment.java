package com.garfield.newsreader.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.baselib.base.BaseFragment;
import com.garfield.newsreader.R;


public class NewsFragment extends BaseFragment {

    private ViewPager mViewPager;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    protected void initView() {
        mViewPager = (ViewPager) getActivity().findViewById(R.id.newsreader_news_viewpager);
        //mViewPager.setAdapter(mFragmentPagerAdapter);
    }


    void test(  ) {

    }
}
