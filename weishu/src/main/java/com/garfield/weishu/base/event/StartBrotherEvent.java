package com.garfield.weishu.base.event;


import com.garfield.baselib.fragmentation.SupportFragment;


public class StartBrotherEvent {
    public SupportFragment targetFragment;

    public StartBrotherEvent(SupportFragment targetFragment) {
        this.targetFragment = targetFragment;
    }
}
