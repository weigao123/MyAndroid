package com.garfield.weishu.base.event;

/**
 * Created by gaowei3 on 2016/10/13.
 */

public interface FragmentJumpEvent {
    void onShowUserProfile(String account);
    void onShowSession(String account);
    void onShowSelfProfile();
    void onShowSearchUser();
    void onShowChangeInfo();
}
