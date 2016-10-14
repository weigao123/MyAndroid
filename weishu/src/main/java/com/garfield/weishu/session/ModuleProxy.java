package com.garfield.weishu.session;

import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by gaowei3 on 2016/10/14.
 */

public interface ModuleProxy {
    boolean sendMessage(IMMessage msg);
    void onInputPanelExpand();
    void shouldCollapseInputPanel();
    boolean isLongClickEnabled();
}
