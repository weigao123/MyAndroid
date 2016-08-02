package com.garfiled.weixin2.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.baselib.base.MiddleFragment;
import com.garfield.baselib.fragmentation.SupportFragment;
import com.garfield.baselib.fragmentation.anim.DefaultNoAnimator;
import com.garfield.baselib.fragmentation.anim.FragmentAnimator;
import com.garfield.baselib.base.BaseFragment;
import com.garfield.baselib.widget.BottomBar;
import com.garfiled.weixin2.R;

/**
 * Created by gaowei3 on 2016/7/31.
 */

/**
 * 不把BottomBar放在Activity中的原因是，要启动一个覆盖BottomBar的MsgFragment，否则无法覆盖
 * MainFragment包含1个BottomBar和3个Tab页Fragment
 */
public class MainFragment extends MiddleFragment implements BottomBar.OnTabSelectedListener {

    private SupportFragment[] mFragments = new SupportFragment[3];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        if (savedInstanceState == null) {
            mFragments[0] = (SupportFragment) Fragment.instantiate(mActivity, MsgListFragment.class.getName());
            mFragments[1] = (SupportFragment) Fragment.instantiate(mActivity, ContactFragment.class.getName());
            mFragments[2] = (SupportFragment) Fragment.instantiate(mActivity, DiscoverFragment.class.getName());
            loadMultiRootFragment(R.id.main_fragment_container, 0, mFragments[0], mFragments[1], mFragments[2]);
        } else {
            mFragments[0] = findFragment(MsgListFragment.class);
            mFragments[1] = findFragment(ContactFragment.class);
            mFragments[2] = findFragment(DiscoverFragment.class);
        }
        initView(view);
        return view;
    }

    private void initView(View view) {
        BottomBar bottomBar = (BottomBar) view.findViewById(R.id.bottomBar);
        bottomBar.setColor(R.color.bottombar_item_unselect, R.color.colorPrimary)
                  .addItem(R.drawable.ic_message_white, "消息")
                  .addItem(R.drawable.ic_contact_white, "联系人")
                  .addItem(R.drawable.ic_discover_white, "发现");
        bottomBar.setOnTabSelectedListener(this);
    }


    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultNoAnimator();
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
}
