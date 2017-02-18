package com.garfield.weishu.nim;

import com.garfield.baselib.ui.dialog.DialogMaker;
import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.weishu.nim.cache.DataCacheManager;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hzxuwen on 2015/9/17.
 */
public class NimHelper {
    private static final String TAG = NimHelper.class.getSimpleName();

    /**
     * 更新用户资料
     */
    public static void updateUserInfo(final UserInfoFieldEnum field, final Object value, final RequestCallbackWrapper<Void> callback) {
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        fields.put(field, value);
        NIMClient.getService(UserService.class)
                .updateUserInfo(fields)
                .setCallback(new RequestCallbackWrapper<Void>() {
                    @Override
                    public void onResult(int code, Void result, Throwable exception) {
                        if (callback != null) {
                            callback.onResult(code, result, exception);
                        }
                    }
        });
    }

    public static void addUserAsFriend(String account, final RequestCallbackWrapper<Void> callback) {
        // workaround, 否则联系人里没有该好友
        if (UserInfoCache.getInstance().getUserInfoByAccount(account) == null) {
            UserInfoCache.getInstance().getUserInfoFromRemote(account, null);
        }
        NIMClient.getService(FriendService.class)
                .addFriend(new AddFriendData(account, VerifyType.DIRECT_ADD, ""))
                .setCallback(new RequestCallbackWrapper<Void>() {
                    @Override
                    public void onResult(int code, Void result, Throwable exception) {
                        if (callback != null) {
                            callback.onResult(code, result, exception);
                        }
                    }
                });
    }

}
