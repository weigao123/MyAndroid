package com.garfield.weishu.discovery.news.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.garfield.baselib.fragmentation.SupportFragment;
import com.garfield.weishu.R;
import com.garfield.weishu.discovery.news.api.ApiManager;
import com.garfield.weishu.discovery.news.api.NeteaseApi;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaowei3 on 2016/8/1.
 */
public class NewsFragment extends AppBaseFragment {

    private List<SupportFragment> mFragments = new ArrayList<>();

    {
        setAnimationEnable(false);
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_news;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.addFragment(NewsListFragment.newInstance(NeteaseApi.NEWS_TYPE_TOP), "头条");
        myPagerAdapter.addFragment(NewsListFragment.newInstance(NeteaseApi.NEWS_TYPE_TECHNOLOGY), "科技");
        myPagerAdapter.addFragment(NewsListFragment.newInstance(NeteaseApi.NEWS_TYPE_ENTERTAINMENT), "娱乐");
        myPagerAdapter.addFragment(NewsListFragment.newInstance(NeteaseApi.NEWS_TYPE_FINANCE), "财经");
        myPagerAdapter.addFragment(NewsListFragment.newInstance(NeteaseApi.NEWS_TYPE_HEALTH), "健康");
        myPagerAdapter.addFragment(NewsListFragment.newInstance(NeteaseApi.NEWS_TYPE_EMOTION), "情感");
        myPagerAdapter.addFragment(NewsListFragment.newInstance(NeteaseApi.NEWS_TYPE_TIANJIN), "天津");
        myPagerAdapter.addFragment(NewsListFragment.newInstance(NeteaseApi.NEWS_TYPE_HOUSE), "房产");
        viewPager.setAdapter(myPagerAdapter);
    }

    static class MyPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
