package com.garfield.weishu.nim;

import android.text.TextUtils;

import com.garfield.baselib.utils.L;
import com.garfield.weishu.AppCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.UserServiceObserve;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gaowei3 on 2016/9/9.
 */
public class UserInfoCache {

    public static UserInfoCache getInstance() {
        return InstanceHolder.instance;
    }

    // 所有用户资料
    private Map<String, NimUserInfo> mUserMap = new ConcurrentHashMap<>();

    // 多处同时获取时，都要有返回
    private Map<String, List<RequestCallback<NimUserInfo>>> mFetchUserInfoMap = new ConcurrentHashMap<>(); // 重复请求处理

    /**
     * 构建缓存与清理
     */
    public void buildCache() {
        //从本地数据库获取
        List<NimUserInfo> users = NIMClient.getService(UserService.class).getAllUserInfo();
        addOrUpdateUsers(users, false);
        L.d("build NimUserInfoCache completed, users count = " + mUserMap.size());
    }

    public void clear() {
        clearUserCache();
    }

    public void registerObservers(boolean register) {
        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(userInfoUpdateObserver, register);
    }

    private Observer<List<NimUserInfo>> userInfoUpdateObserver = new Observer<List<NimUserInfo>>() {
        @Override
        public void onEvent(List<NimUserInfo> users) {
            if (users == null || users.isEmpty()) {
                return;
            }

            addOrUpdateUsers(users, true);
        }
    };

    private void addOrUpdateUsers(final List<NimUserInfo> users, boolean notify) {
        if (users == null || users.isEmpty()) {
            return;
        }

        // update cache
        for (NimUserInfo u : users) {
            mUserMap.put(u.getAccount(), u);
        }

        // log
        List<String> accounts = getAccounts(users);

        // 通知变更
        if (notify && accounts != null && !accounts.isEmpty()) {
            //NimUIKit.notifyUserInfoChanged(accounts); // 通知到UI组件
        }
    }


    /**
     * 从云信服务器获取用户信息（重复请求处理）[异步]
     */
    public void getUserInfoFromRemote(final String account, final RequestCallback<NimUserInfo> callback) {
        if (TextUtils.isEmpty(account)) {
            return;
        }

        if (mFetchUserInfoMap.containsKey(account)) {
            if (callback != null) {
                mFetchUserInfoMap.get(account).add(callback);
            }
            return; // 已经在请求中，不要重复请求
        } else {
            List<RequestCallback<NimUserInfo>> cbs = new ArrayList<>();
            if (callback != null) {
                cbs.add(callback);
            }
            mFetchUserInfoMap.put(account, cbs);
        }

        List<String> accounts = new ArrayList<>(1);
        accounts.add(account);

        NIMClient.getService(UserService.class).fetchUserInfo(accounts).setCallback(new RequestCallbackWrapper<List<NimUserInfo>>() {

            @Override
            public void onResult(int code, List<NimUserInfo> users, Throwable exception) {
                if (exception != null) {
                    callback.onException(exception);
                    return;
                }

                NimUserInfo user = null;
                boolean hasCallback = mFetchUserInfoMap.get(account).size() > 0;
                if (code == ResponseCode.RES_SUCCESS && users != null && !users.isEmpty()) {
                    user = users.get(0);
                    // 这里不需要更新缓存，由监听SDK用户资料变更（添加）来更新缓存
                }

                // 处理回调
                if (hasCallback) {
                    List<RequestCallback<NimUserInfo>> cbs = mFetchUserInfoMap.get(account);
                    for (RequestCallback<NimUserInfo> cb : cbs) {
                        if (code == ResponseCode.RES_SUCCESS) {
                            cb.onSuccess(user);
                        } else {
                            cb.onFailed(code);
                        }
                    }
                }

                mFetchUserInfoMap.remove(account);
            }
        });
    }

    public void getUserInfoFromRemote(List<String> accounts, final RequestCallback<List<NimUserInfo>> callback) {
        NIMClient.getService(UserService.class).fetchUserInfo(accounts).setCallback(new RequestCallback<List<NimUserInfo>>() {
            @Override
            public void onSuccess(List<NimUserInfo> users) {
                // 这里不需要更新缓存，由监听用户资料变更（添加）来更新缓存
                if (callback != null) {
                    callback.onSuccess(users);
                }
            }

            @Override
            public void onFailed(int code) {
                if (callback != null) {
                    callback.onFailed(code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                if (callback != null) {
                    callback.onException(exception);
                }
            }
        });
    }


    private List<String> getAccounts(List<NimUserInfo> users) {
        if (users == null || users.isEmpty()) {
            return null;
        }

        List<String> accounts = new ArrayList<>(users.size());
        for (NimUserInfo user : users) {
            accounts.add(user.getAccount());
        }

        return accounts;
    }

    public List<NimUserInfo> getAllUsersOfMyFriend() {
        List<String> accounts = FriendDataCache.getInstance().getMyFriendAccounts();
        List<NimUserInfo> users = new ArrayList<>();
        for (String account : accounts) {
            if (hasUser(account)) {
                users.add(getUserInfo(account));
            }
        }
        return users;
    }

    public NimUserInfo getUserInfo(String account) {
        if (TextUtils.isEmpty(account) || mUserMap == null) {
            return null;
        }
        return mUserMap.get(account);
    }

    public void getUserInfo(String account, RequestCallback<NimUserInfo> callback) {
        if (TextUtils.isEmpty(account)) {
            callback.onSuccess(null);
            return;
        }
        NimUserInfo userInfo = mUserMap.get(account);
        if (userInfo != null) {
            callback.onSuccess(userInfo);
            return;
        }
        getUserInfoFromRemote(account, callback);
    }

    public boolean hasUser(String account) {
        if (TextUtils.isEmpty(account) || mUserMap == null) {
            return false;
        }
        return mUserMap.containsKey(account);
    }

    /**
     * 获取用户显示名称。
     * 若设置了备注名，则显示备注名。
     * 若没有设置备注名，用户有昵称则显示昵称，用户没有昵称则显示帐号。
     */
    public String getUserDisplayName(String account) {
        String alias = getAlias(account);
        if (!TextUtils.isEmpty(alias)) {
            return alias;
        }

        return getUserName(account);
    }

    public String getAlias(String account) {
        Friend friend = FriendDataCache.getInstance().getFriendByAccount(account);
        if (friend != null && !TextUtils.isEmpty(friend.getAlias())) {
            return friend.getAlias();
        }
        return null;
    }

    // 获取用户原本的昵称
    public String getUserName(String account) {
        NimUserInfo userInfo = getUserInfo(account);
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getName())) {
            return userInfo.getName();
        } else {
            return account;
        }
    }

    public String getUserDisplayNameEx(String account) {
        if (account.equals(AppCache.getAccount())) {
            return "我";
        }

        return getUserDisplayName(account);
    }

    public String getUserDisplayNameYou(String account) {
        if (account.equals(AppCache.getAccount())) {
            return "你";  // 若为用户自己，显示“你”
        }

        return getUserDisplayName(account);
    }

    private void clearUserCache() {
        mUserMap.clear();
    }

    static class InstanceHolder {
        final static UserInfoCache instance = new UserInfoCache();
    }
}
