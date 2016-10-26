package com.garfield.weishu.nim;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;

import java.util.List;

/**
 * Created by gaowei3 on 2016/10/14.
 */

public class NimUtils {
    public static void sendMessage(IMMessage message) {

    }

    public static void loadMessageFromLocal(String account, RequestCallbackWrapper<List<IMMessage>> callback) {
        IMMessage imMessage = MessageBuilder.createEmptyMessage(account, SessionTypeEnum.P2P, 0);
        NIMClient.getService(MsgService.class).queryMessageListEx(imMessage, QueryDirectionEnum.QUERY_OLD, 20, true)
                .setCallback(callback);
    }
}
