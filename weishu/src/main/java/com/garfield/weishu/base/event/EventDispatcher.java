package com.garfield.weishu.base.event;

import com.garfield.weishu.ui.fragment.ChangeInfoFragment;
import com.garfield.weishu.ui.fragment.FriendProfileFragment;
import com.garfield.weishu.ui.fragment.SearchUserFragment;
import com.garfield.weishu.ui.fragment.SelfProfileFragment;
import com.garfield.weishu.ui.fragment.SessionFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by gaowei3 on 2016/10/13.
 */

public class EventDispatcher {

    private static FragmentJumpEvent fragmentJumpEvent;

    static {
        fragmentJumpEvent = new FragmentJumpEvent() {
            @Override
            public void onShowUserProfile(String account) {
                EventBus.getDefault().post(new StartBrotherEvent(FriendProfileFragment.newInstance(account)));
            }

            @Override
            public void onShowSession(String account) {
                EventBus.getDefault().post(new StartBrotherEvent(SessionFragment.newInstance(account)));
            }

            @Override
            public void onShowSelfProfile() {
                EventBus.getDefault().post(new StartBrotherEvent(new SelfProfileFragment()));
            }

            @Override
            public void onShowSearchUser() {
                EventBus.getDefault().post(new StartBrotherEvent(new SearchUserFragment()));
            }

            @Override
            public void onShowChangeInfo() {
                EventBus.getDefault().post(new StartBrotherEvent(new ChangeInfoFragment()));
            }
        };
    }

    public static FragmentJumpEvent getFragmentJumpEvent() {
        return fragmentJumpEvent;
    }

}

