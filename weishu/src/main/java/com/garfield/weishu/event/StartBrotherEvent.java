package com.garfield.weishu.event;


import com.garfield.baselib.fragmentation.SupportFragment;


public class StartBrotherEvent {
    public SupportFragment targetFragment;

    public StartBrotherEvent(SupportFragment targetFragment) {
        this.targetFragment = targetFragment;
    }
}
