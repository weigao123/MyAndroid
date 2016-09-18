package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;

import com.garfield.baselib.fragmentation.SupportFragment;
import com.garfield.baselib.fragmentation.anim.DefaultHorizontalAnimator;
import com.garfield.baselib.fragmentation.anim.FragmentAnimator;
import com.garfield.baselib.ui.widget.BottomBar;
import com.garfield.weishu.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by gaowei3 on 2016/7/31.
 */

/**
 * 不把BottomBar放在Activity中的原因是，要启动一个覆盖BottomBar的MsgFragment，否则无法覆盖
 * MainFragment包含1个BottomBar和3个Tab页Fragment
 */
public class MainFragment extends AppBaseFragment implements BottomBar.OnTabSelectedListener {

    private SupportFragment[] mFragments = new SupportFragment[4];

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        if (mToolbarControl != null) {
            mToolbarControl.setVisibility(View.VISIBLE);
        }
        if (savedInstanceState == null) {
            mFragments[0] = new MsgListFragment();
            mFragments[1] = new ContactFragment();
            mFragments[2] = new NewsListFragment();
            mFragments[3] = new SettingFragment();
            loadMultiRootFragment(R.id.main_fragment_container, 0, mFragments[0], mFragments[1], mFragments[2], mFragments[3]);
        } else {
            mFragments[0] = findFragment(MsgListFragment.class);
            mFragments[1] = findFragment(ContactFragment.class);
            mFragments[2] = findFragment(NewsListFragment.class);
            mFragments[3] = findFragment(SettingFragment.class);
        }

        BottomBar bottomBar = (BottomBar) rootView.findViewById(R.id.bottomBar);
        bottomBar.setColor(R.color.bottombar_item_unselect, R.color.colorPrimary)
                .addItem(R.drawable.ic_message_white, "消息")
                .addItem(R.drawable.ic_contact_white, "联系人")
                .addItem(R.drawable.ic_news_white, "新闻")
                .addItem(R.drawable.ic_settings_white, "设置");
        bottomBar.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(int position, int prePosition) {
        showHideFragment(mFragments[position], mFragments[prePosition]);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     *  如果是根元素就不去动画，在loadRootFragment时没有设置setTransition
     */
    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //L.d("MainFragment  transit: "+transit+"  enter:"+enter+"   nextAnim:"+nextAnim);
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
}
