package com.garfield.weishu.base.event;

import com.garfield.weishu.ui.fragment.ChangeInfoFragment;
import com.garfield.weishu.ui.fragment.CropPhotoFragment;
import com.garfield.weishu.ui.fragment.FriendProfileFragment;
import com.garfield.weishu.ui.fragment.SearchUserFragment;
import com.garfield.weishu.ui.fragment.SelfProfileFragment;
import com.garfield.weishu.ui.fragment.SessionFragment;
import com.garfield.weishu.ui.fragment.TakePhotoFragment;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by gaowei3 on 2016/10/13.
 */

public class EventDispatcher {

    private static MyEvent.FragmentJumpEvent fragmentJumpEvent;

    static {
        fragmentJumpEvent = new MyEvent.FragmentJumpEvent() {
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

            @Override
            public void onShowTakePhoto() {
                EventBus.getDefault().post(new StartBrotherEvent(new TakePhotoFragment()));
            }

            @Override
            public void onShowCropPhoto(String photoPath) {
                EventBus.getDefault().post(new StartBrotherEvent(CropPhotoFragment.newInstance(photoPath)));
            }
        };
    }

    public static MyEvent.FragmentJumpEvent getFragmentJumpEvent() {
        return fragmentJumpEvent;
    }

}

