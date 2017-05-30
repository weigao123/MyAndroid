package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.baselib.fragmentation.SupportFragment;
import com.garfield.baselib.ui.widget.BottomBarTransition;
import com.garfield.weishu.BuildConfig;
import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.contact.ContactFragment;
import com.garfield.weishu.developer.test.TestViewPagerFragment;
import com.garfield.weishu.discovery.DiscoveryFragment;
import com.garfield.weishu.nim.NimConfig;
import com.garfield.weishu.session.sessionlist.SessionListFragment;
import com.garfield.weishu.setting.SettingFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/7/31.
 */

/**
 * 不把BottomBar放在Activity中的原因是，要启动一个覆盖BottomBar的MsgFragment，否则无法覆盖
 * MainFragment包含1个BottomBar和3个Tab页Fragment
 */
public class MainFragment extends AppBaseFragment implements BottomBarTransition.OnTabSelectedListener {

    private SupportFragment[] mFragments = new SupportFragment[4];

    @BindView(R.id.bottomBar)
    BottomBarTransition mBottomBar;

    @BindView(R.id.main_fragment_container)
    ViewPager mViewPager;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mFragments[0] = new SessionListFragment();
            mFragments[1] = new ContactFragment();
            mFragments[2] = new DiscoveryFragment();
            mFragments[3] = new SettingFragment();
        } else {
            List<Fragment> fragments = getChildFragmentManager().getFragments();
            mFragments = fragments.toArray(mFragments);
        }
        /**
         * 转屏时PagerAdapter里的FragmentManager会记住所有getItem打开过的实例，重建时自动显示所有的Fragment，所以不用管
         * 而且只有未加载的Fragment才会调用getItem，因此即使mFragments是null也没事，只要打开过的就不再getItem获取
         *
         * 转屏时Fragment下的所有的子View会重建，ViewPager这时恢复上次的savedInstanceState(只存了position)，进而恢复原来的位置
         * 但是没保存PagerAdapter，所以还要重新创建
         */
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getChildFragmentManager(), mFragments);
        mViewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

        mBottomBar.setColor(R.color.bottom_bar_unselected, R.color.bottom_bar_selected, R.color.horizontal_divider, R.color.bottom_bar_bg)
                .addItem(R.drawable.ic_bottom_message1, R.drawable.ic_bottom_message2, R.string.message)
                .addItem(R.drawable.ic_bottom_contact1, R.drawable.ic_bottom_contact2, R.string.contact)
                .addItem(R.drawable.ic_bottom_news1, R.drawable.ic_bottom_news2, R.string.discovery)
                .addItem(R.drawable.ic_bottom_personal1, R.drawable.ic_bottom_personal2, R.string.personal);
        mBottomBar.setOnTabSelectedListener(this);
    }

    public void switchToFirst() {
        mViewPager.setCurrentItem(0, false);
        updateNotification(0);
    }

    @Override
    public void onTabSelected(int position, int prePosition) {
        mViewPager.setCurrentItem(position, false);
    }

    @Override
    public void onTabReselected(int position) {
        if (BuildConfig.DEBUG && position == 3) {
            EventDispatcher.startFragmentEvent(new TestViewPagerFragment());
        }
    }

    private void updateNotification(int position) {
        if (position == 0) {
            NimConfig.nofityWithNoTopBar();
        } else {
            NimConfig.nofityWithTopBar();
        }
    }

    public int getTabPosition() {
        return mViewPager != null ? mViewPager.getCurrentItem() : 0;
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //position永远是两个正在切换中的左边那个
            mBottomBar.setTabShifting(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
            updateNotification(position);
            mBottomBar.setTabSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private final SupportFragment[] mFragments;

        MyPagerAdapter(FragmentManager fm, SupportFragment[] fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            /**
             * 阻止销毁，防止重建fragment，发现不能去掉，否则转屏时有的页面无法显示
             * 可以由setOffscreenPageLimit代替阻止销毁
             */
            super.destroyItem(container, position, object);
        }
    }

}
