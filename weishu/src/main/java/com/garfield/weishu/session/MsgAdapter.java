package com.garfield.weishu.session;

import com.garfield.weishu.base.TAdapter;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.Set;

/**
 * Created by gwball on 2016/9/28.
 */

public class MsgAdapter extends TAdapter {

    private Set<String> timedItems; // 需要显示消息时间的消息ID
    private IMMessage lastShowTimeItem; // 用于消息时间显示,判断和上条消息间的时间间隔

    public boolean needShowTime(IMMessage message) {
        return timedItems.contains(message.getUuid());
    }
}
