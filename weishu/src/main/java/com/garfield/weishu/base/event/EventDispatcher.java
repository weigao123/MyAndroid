package com.garfield.weishu.base.event;

import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;

import com.garfield.baselib.fragmentation.SupportFragment;
import com.garfield.baselib.utils.drawable.UiUtils;
import com.garfield.weishu.discovery.news.ui.NewsDetailFragment;
import com.garfield.weishu.discovery.news.ui.NewsFragment;
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

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    private static MyEvent.FragmentJumpEvent mFragmentJumpEvent = new MyEvent.FragmentJumpEvent() {
        @Override
        public void onShowUserProfile(String account) {
            startFragment(FriendProfileFragment.newInstance(account));
        }

        @Override
        public void onShowSession(String account) {
            startFragment(SessionFragment.newInstance(account));
        }

        @Override
        public void onShowSelfProfile() {
            startFragment(new SelfProfileFragment());
        }

        @Override
        public void onShowSearchUser() {
            startFragment(new SearchUserFragment());
        }

        @Override
        public void onShowChangeInfo() {
            startFragment(new ChangeInfoFragment());
        }

        @Override
        public void onShowTakePhoto() {
            startFragment(new TakePhotoFragment());
        }

        @Override
        public void onShowCropPhoto(String photoPath) {
            startFragment(CropPhotoFragment.newInstance(photoPath));
        }

        @Override
        public void onShowFullscreenPhoto(String photoPath) {
            startFragment(FullscreenPhoto.newInstance(photoPath));
        }

        @Override
        public void onShowNews() {
            startFragment(new NewsFragment());
        }

        @Override
        public void onShowNewsDetail(String url) {
            startFragment(NewsDetailFragment.newInstance(url));
        }
    };


    public static void startFragment(final SupportFragment fragment) {
        if (UiUtils.isFastDoubleClick(500)) return;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new StartBrotherEvent(fragment));
            }
        }, 100);
    }

    public static void startDialog(final Dialog dialog) {
        if (UiUtils.isFastDoubleClick(500)) return;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        }, 100);
    }

    public static MyEvent.FragmentJumpEvent getFragmentJumpEvent() {
        return mFragmentJumpEvent;
    }



}

