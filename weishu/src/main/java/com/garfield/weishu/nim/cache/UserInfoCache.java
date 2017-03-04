package com.garfield.weishu.nim.cache;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.garfield.baselib.utils.system.L;
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

    public static final String TAG = UserInfoCache.class.getSimpleName();

    // 所有用户资料，只要fetch过就会有
    private Map<String, NimUserInfo> mUserMap = new ConcurrentHashMap<>();

    // app层
    private List<UserInfoChangedObserver> userInfoObservers = new ArrayList<>();

    // 回调列表，多处同时获取时，都要有返回
    private Map<String, List<RequestCallback<NimUserInfo>>> mFetchRequestMap = new ConcurrentHashMap<>(); // 重复请求处理

    /**
     * 构建缓存与清理
     */
    public void buildCache() {
        //从本地数据库获取
        List<NimUserInfo> users = NIMClient.getService(UserService.class).getAllUserInfo();
        for (NimUserInfo u : users) {
            mUserMap.put(u.getAccount(), u);
            //L.d(TAG, "buildCache account: "+u.getAccount());
        }
    }

    public void clear() {
        mUserMap.clear();
    }

    void registerSDKObservers(boolean register) {
        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(userInfoSDKUpdateObserver, register);
    }

    /**
     * 缓存层，监听SDK层变化，然后马上通知app层
     */
    private Observer<List<NimUserInfo>> userInfoSDKUpdateObserver = new Observer<List<NimUserInfo>>() {
        @Override
        public void onEvent(List<NimUserInfo> users) {
            if (users == null || users.isEmpty()) {
                return;
            }
            for (NimUserInfo u : users) {
                mUserMap.put(u.getAccount(), u);
                L.d(TAG, "userInfo updateUserInfo account: "+u.getAccount());
            }
            List<String> accounts = getAccountsFromUserInfo(users);
            if (accounts != null && !accounts.isEmpty()) {
                for (UserInfoChangedObserver o : userInfoObservers) {
                    o.onUserInfoChanged(accounts);
                }
            }
        }
    };

    private List<String> getAccountsFromUserInfo(List<NimUserInfo> users) {
        if (users == null || users.isEmpty()) {
            return null;
        }
        List<String> accounts = new ArrayList<>(users.size());
        for (NimUserInfo user : users) {
            accounts.add(user.getAccount());
        }
        return accounts;
    }

    /**
     * app层监听缓存层变化，fetch后也会回调的
     */
    public void registerUserInfoChangedObserver(UserInfoChangedObserver o, boolean register) {
        if (o == null) {
            return;
        }
        if (register) {
            if (!userInfoObservers.contains(o)) {
                userInfoObservers.add(o);
            }
        } else {
            userInfoObservers.remove(o);
        }
    }

    public interface UserInfoChangedObserver {
        void onUserInfoChanged(List<String> accounts);
    }

    /**
     * 获取一个用户，从云信服务器获取用户信息（重复请求处理）[异步]
     */
    public void getUserInfoFromRemote(final String account, final RequestCallback<NimUserInfo> callback) {
        if (TextUtils.isEmpty(account)) {
            return;
        }

        if (mFetchRequestMap.containsKey(account)) {
            if (callback != null) {
                mFetchRequestMap.get(account).add(callback);
            }
            return; // 已经在请求中，只需要加入回调列表，不要重复请求
        } else {
            List<RequestCallback<NimUserInfo>> cbs = new ArrayList<>();
            if (callback != null) {
                cbs.add(callback);
            }
            mFetchRequestMap.put(account, cbs);
        }

        List<String> accounts = new ArrayList<>(1);
        accounts.add(account);

        NIMClient.getService(UserService.class).fetchUserInfo(accounts).setCallback(new RequestCallbackWrapper<List<NimUserInfo>>() {

            @Override
            public void onResult(int code, List<NimUserInfo> users, Throwable exception) {
                if (exception != null && callback != null) {
                    callback.onException(exception);
                    return;
                }

                NimUserInfo user = null;
                boolean hasCallback = mFetchRequestMap.get(account).size() > 0;
                if (code == ResponseCode.RES_SUCCESS && users != null && !users.isEmpty()) {
                    user = users.get(0);
                    // 这里不需要更新缓存mUserMap，由监听SDK用户资料变更（添加）来更新缓存
                }

                // 处理回调
                if (hasCallback) {
                    List<RequestCallback<NimUserInfo>> cbs = mFetchRequestMap.get(account);
                    for (RequestCallback<NimUserInfo> cb : cbs) {
                        if (code == ResponseCode.RES_SUCCESS) {
                            cb.onSuccess(user);
                        } else {
                            cb.onFailed(code);
                        }
                    }
                }

                // 处理完拿掉回调列表
                mFetchRequestMap.remove(account);
            }
        });
    }

    /**
     * 同时获取多个用户，从云信服务器获取用户信息（重复请求处理）[异步]
     */
    public void getUsersInfoFromRemote(List<String> accounts, final RequestCallback<List<NimUserInfo>> callback) {
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

    /**
     * 如果是好友，就默认会在UserInfo里吗？不会，必须得fetch才有
     * 如果直接添加好友，没有fetch，会造成mUserMap没有该账号
     */
    public List<NimUserInfo> getUserInfoOfAllMyFriend() {
        List<String> accounts = FriendDataCache.getInstance().getAllFriendAccounts();
        List<NimUserInfo> users = new ArrayList<>();
        for (String account : accounts) {
            if (mUserMap.containsKey(account)) {
                users.add(getUserInfoByAccount(account));
            }
        }
        return users;
    }

    // 可能是null
    public NimUserInfo getUserInfoByAccount(String account) {
        if (TextUtils.isEmpty(account) || mUserMap == null) {
            return null;
        }
        return mUserMap.get(account);
    }

    public void getUserInfoByAccountSafe(String account, RequestCallback<NimUserInfo> callback) {
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

    /**
     * 若设置了备注名，则显示备注名。
     * 若没有设置备注名，用户有昵称则显示昵称，用户没有昵称则显示帐号。
     */
    public String getUserDisplayName(String account) {
        String alias = getUserAlias(account);
        if (!TextUtils.isEmpty(alias)) {
            return alias;
        }
        return getUserName(account);
    }

    // 备注，在Friend里
    public String getUserAlias(String account) {
        Friend friend = FriendDataCache.getInstance().getFriendByAccount(account);
        if (friend != null && !TextUtils.isEmpty(friend.getAlias())) {
            return friend.getAlias();
        }
        return null;
    }

    // 用户的名字，在UserInfo里
    public String getUserName(String account) {
        NimUserInfo userInfo = getUserInfoByAccount(account);
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getName())) {
            return userInfo.getName();
        }
        return account;
    }

    public static UserInfoCache getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        final static UserInfoCache instance = new UserInfoCache();
    }
}
