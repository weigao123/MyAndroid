package com.garfield.weishu.session.session;

import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by gaowei3 on 2016/10/14.
 */

public interface ModuleProxy {
    boolean sendMessage(IMMessage msg);
    void shouldCollapseInputPanel();
    boolean isLongClickEnabled();
}
