package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.garfield.baselib.fragmentation.SupportFragment;
import com.garfield.baselib.fragmentation.anim.DefaultHorizontalAnimator;
import com.garfield.baselib.fragmentation.anim.FragmentAnimator;
import com.garfield.baselib.ui.widget.BottomBar;
import com.garfield.weishu.R;
import com.garfield.weishu.event.StartBrotherEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        if (savedInstanceState == null) {
            mFragments[0] = new MsgListFragment();
            mFragments[1] = new ContactListFragment();
            mFragments[2] = new NewsListFragment();
            mFragments[3] = new SettingFragment();
            loadMultiRootFragment(R.id.main_fragment_container, 0, mFragments[0], mFragments[1], mFragments[2], mFragments[3]);
        } else {
            mFragments[0] = findFragment(MsgListFragment.class);
            mFragments[1] = findFragment(ContactListFragment.class);
            mFragments[2] = findFragment(NewsListFragment.class);
            mFragments[3] = findFragment(SettingFragment.class);
        }
        EventBus.getDefault().register(this);
        initView(view);
        return view;
    }

    private void initView(View view) {
        BottomBar bottomBar = (BottomBar) view.findViewById(R.id.bottomBar);
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

    @Subscribe
    public void onEvent(StartBrotherEvent event) {
        startFragment(event.targetFragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

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
