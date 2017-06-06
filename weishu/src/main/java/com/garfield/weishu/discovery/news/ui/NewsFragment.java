package com.garfield.weishu.discovery.news.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.garfield.weishu.R;
import com.garfield.weishu.base.viewpager.fragment.FmAdapter;
import com.garfield.weishu.discovery.news.api.NeteaseApi;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

/**
 * Created by gaowei3 on 2016/8/1.
 */
public class NewsFragment extends AppBaseFragment {

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
        FmAdapter pagerAdapter = new FmAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(NewsListFragment.newInstance(NeteaseApi.NEWS_TYPE_TOP), "头条");
        pagerAdapter.addFragment(NewsListFragment.newInstance(NeteaseApi.NEWS_TYPE_TECHNOLOGY), "科技");
        pagerAdapter.addFragment(NewsListFragment.newInstance(NeteaseApi.NEWS_TYPE_ENTERTAINMENT), "娱乐");
        pagerAdapter.addFragment(NewsListFragment.newInstance(NeteaseApi.NEWS_TYPE_FINANCE), "财经");
        pagerAdapter.addFragment(NewsListFragment.newInstance(NeteaseApi.NEWS_TYPE_HEALTH), "健康");
        pagerAdapter.addFragment(NewsListFragment.newInstance(NeteaseApi.NEWS_TYPE_EMOTION), "情感");
        pagerAdapter.addFragment(NewsListFragment.newInstance(NeteaseApi.NEWS_TYPE_TIANJIN), "天津");
        pagerAdapter.addFragment(NewsListFragment.newInstance(NeteaseApi.NEWS_TYPE_HOUSE), "房产");
        viewPager.setAdapter(pagerAdapter);
    }

}
