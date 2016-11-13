package com.garfield.weishu.base.event;

import com.garfield.weishu.news.view.NewsDetailFragment;
import com.garfield.weishu.setting.ChangeInfoFragment;
import com.garfield.weishu.setting.CropPhotoFragment;
import com.garfield.weishu.contact.FriendProfileFragment;
import com.garfield.weishu.contact.SearchUserFragment;
import com.garfield.weishu.setting.SelfProfileFragment;
import com.garfield.weishu.session.session.SessionFragment;
import com.garfield.weishu.setting.TakePhotoFragment;
import com.garfield.weishu.ui.fragment.FullscreenPhoto;

import org.greenrobot.eventbus.EventBus;

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

            @Override
            public void onShowFullscreenPhoto(String photoPath) {
                EventBus.getDefault().post(new StartBrotherEvent(FullscreenPhoto.newInstance(photoPath)));
            }

            @Override
            public void onShowNewsDetail(String url) {
                EventBus.getDefault().post(new StartBrotherEvent(NewsDetailFragment.newInstance(url)));
            }
        };
    }

    public static MyEvent.FragmentJumpEvent getFragmentJumpEvent() {
        return fragmentJumpEvent;
    }

}

