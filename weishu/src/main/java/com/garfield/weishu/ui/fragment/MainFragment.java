package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.baselib.fragmentation.SupportFragment;
import com.garfield.baselib.fragmentation.anim.DefaultHorizontalAnimator;
import com.garfield.baselib.fragmentation.anim.FragmentAnimator;
import com.garfield.baselib.ui.widget.BottomBar;
import com.garfield.weishu.R;
import com.garfield.weishu.contact.ContactFragment;
import com.garfield.weishu.news.view.NewsListFragment;
import com.garfield.weishu.nim.NimConfig;
import com.garfield.weishu.session.sessionlist.SessionListFragment;
import com.garfield.weishu.setting.SettingFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/7/31.
 */

/**
 * 不把BottomBar放在Activity中的原因是，要启动一个覆盖BottomBar的MsgFragment，否则无法覆盖
 * MainFragment包含1个BottomBar和3个Tab页Fragment
 */
public class MainFragment extends AppBaseFragment implements BottomBar.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    private SupportFragment[] mFragments = new SupportFragment[4];
    private BottomBar mBottomBar;

    @BindView(R.id.main_fragment_container)
    ViewPager mViewPager;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        if (mToolbarControl != null) {
            mToolbarControl.setVisibility(View.VISIBLE);
        }
        mFragments[0] = new SessionListFragment();
        mFragments[1] = new ContactFragment();
        mFragments[2] = new NewsListFragment();
        mFragments[3] = new SettingFragment();

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getChildFragmentManager(), mFragments);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        mBottomBar = (BottomBar) rootView.findViewById(R.id.bottomBar);
        mBottomBar.setColor(R.color.bottombar_item_unselect, R.color.colorPrimary)
                .addItem(R.drawable.ic_message_white, "消息")
                .addItem(R.drawable.ic_contact_white, "联系人")
                .addItem(R.drawable.ic_news_white, "新闻")
                .addItem(R.drawable.ic_settings_white, "设置");
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
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updateNotification(position);
        mBottomBar.setTabSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

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
             * 防止重建fragment
             */
            //super.destroyItem(container, position, object);
        }
    }

    /**
     *  如果是根元素就不去动画，在loadRootFragment时没有设置setTransition
     */
    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);
//        Class topClass = getTopFragment().getClass();
//        if (topClass == SessionFragment.class) {
//            if (transit == FragmentTransaction.TRANSIT_FRAGMENT_OPEN && !enter && animation.getDuration() > 100) {
//                animation.setStartOffset(300);
//            }
//        }
//        return animation;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
