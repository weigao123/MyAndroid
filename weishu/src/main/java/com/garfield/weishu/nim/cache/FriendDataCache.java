package com.garfield.weishu.nim.cache;

import android.text.TextUtils;

import com.garfield.baselib.utils.L;
import com.garfield.weishu.AppCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.FriendServiceObserve;
import com.netease.nimlib.sdk.friend.model.BlackListChangedNotify;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.friend.model.FriendChangedNotify;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * 好友关系缓存
 * 注意：获取通讯录列表即是根据Friend列表帐号，去取对应的UserInfo
 */
public class FriendDataCache {

    public static final String TAG = FriendDataCache.class.getSimpleName();

    // 全部好友，包括黑名单和自己
    private Map<String, Friend> allFriendMap = new ConcurrentHashMap<>();

    // 精简好友，排除了黑名单和自己
    private Set<String> simplifyFriendSet = new CopyOnWriteArraySet<>();

    private List<FriendDataChangedObserver> friendObservers = new ArrayList<>();

    public void buildCache() {
        // 获取我所有的好友关系
        List<Friend> friends = NIMClient.getService(FriendService.class).getFriends();
        for (Friend f : friends) {
            allFriendMap.put(f.getAccount(), f);
            L.d(TAG, "buildCache account: "+f.getAccount());
        }
        // 获取我所有好友的帐号
        List<String> accounts = NIMClient.getService(FriendService.class).getFriendAccounts();
        if (accounts == null || accounts.isEmpty()) {
            return;
        }
        // 排除黑名单
        List<String> blacks = NIMClient.getService(FriendService.class).getBlackList();
        accounts.removeAll(blacks);
        // 排除掉自己
        accounts.remove(AppCache.getAccount());
        simplifyFriendSet.addAll(accounts);
    }

    public void clear() {
        simplifyFriendSet.clear();
        allFriendMap.clear();
    }

    /**
     * ****************************** 好友查询接口 ******************************
     */
    public List<String> getAllFriendAccounts() {
        List<String> accounts = new ArrayList<>(simplifyFriendSet.size());
        accounts.addAll(simplifyFriendSet);

        return accounts;
    }

    public int getAllFriendCounts() {
        return simplifyFriendSet.size();
    }

    public Friend getFriendByAccount(String account) {
        if (TextUtils.isEmpty(account)) {
            return null;
        }
        return allFriendMap.get(account);
    }

    /**
     * 排除了黑名单和自己
     */
    public boolean isMyFriend(String account) {
        return simplifyFriendSet.contains(account);
    }

    /**
     * 缓存层，监听SDK层变化
     */
    public void registerSDKObservers(boolean register) {
        // 主动添加好友成功、被添加为好友、主动删除好友成功、被对方解好友关系、好友关系更新、登录同步好友关系数据时
        NIMClient.getService(FriendServiceObserve.class).observeFriendChangedNotify(friendChangedNotifyObserver, register);
        NIMClient.getService(FriendServiceObserve.class).observeBlackListChangedNotify(blackListChangedNotifyObserver, register);
    }

    /**
     * APP层，监听缓存层变化
     */
    public void registerFriendDataChangedObserver(FriendDataChangedObserver o, boolean register) {
        if (o == null) {
            return;
        }
        if (register) {
            if (!friendObservers.contains(o)) {
                friendObservers.add(o);
            }
        } else {
            friendObservers.remove(o);
        }
    }

    public interface FriendDataChangedObserver {
        void onAddedOrUpdatedFriends(List<String> accounts);
        void onDeletedFriends(List<String> accounts);
        void onAddUserToBlackList(List<String> account);
        void onRemoveUserFromBlackList(List<String> account);
    }

    /**
     * 监听好友关系变化
     */
    private Observer<FriendChangedNotify> friendChangedNotifyObserver = new Observer<FriendChangedNotify>() {
        @Override
        public void onEvent(FriendChangedNotify friendChangedNotify) {
            List<Friend> addedOrUpdatedFriends = friendChangedNotify.getAddedOrUpdatedFriends();
            List<String> deletedFriendAccounts = friendChangedNotify.getDeletedFriends();
            // 排除了黑名单的更新的好友
            List<String> simplifyFriendAccounts = new ArrayList<>(addedOrUpdatedFriends.size());
            // 全部更新的好友
            List<String> allFriendAccounts = new ArrayList<>(addedOrUpdatedFriends.size());

            // 追加到全部好友列表
            String account;
            for (Friend f : addedOrUpdatedFriends) {
                account = f.getAccount();
                allFriendMap.put(account, f);
                allFriendAccounts.add(account);
                L.d(TAG, "friend update account: "+f.getAccount());

                if (NIMClient.getService(FriendService.class).isInBlackList(account)) {
                    continue;
                }
                simplifyFriendAccounts.add(account);
            }

            // 追加到精简好友列表
            if (!simplifyFriendAccounts.isEmpty()) {
                simplifyFriendSet.addAll(simplifyFriendAccounts);
            }

            // 通知
            if (!allFriendAccounts.isEmpty()) {
                for (FriendDataChangedObserver o : friendObservers) {
                    o.onAddedOrUpdatedFriends(allFriendAccounts);
                }
            }

            // 处理被删除的好友关系
            if (!deletedFriendAccounts.isEmpty()) {
                simplifyFriendSet.removeAll(deletedFriendAccounts);
                for (String a : deletedFriendAccounts) {
                    allFriendMap.remove(a);
                }
                // 通知
                for (FriendDataChangedObserver o : friendObservers) {
                    o.onDeletedFriends(deletedFriendAccounts);
                }
            }
        }
    };

    /**
     * 监听黑名单变化(决定是否加入或者移出好友列表)
     */
    private Observer<BlackListChangedNotify> blackListChangedNotifyObserver = new Observer<BlackListChangedNotify>() {
        @Override
        public void onEvent(BlackListChangedNotify blackListChangedNotify) {
            List<String> addedAccounts = blackListChangedNotify.getAddedAccounts();
            List<String> removedAccounts = blackListChangedNotify.getRemovedAccounts();

            if (!addedAccounts.isEmpty()) {
                // 只删除精简好友列表
                simplifyFriendSet.removeAll(addedAccounts);

                // 通知
                for (FriendDataChangedObserver o : friendObservers) {
                    o.onAddUserToBlackList(addedAccounts);
                }

                // 拉黑，要从最近联系人列表中删除该好友
                for (String account : addedAccounts) {
                    NIMClient.getService(MsgService.class).deleteRecentContact2(account, SessionTypeEnum.P2P);
                }
            }

            if (!removedAccounts.isEmpty()) {
                // 移出黑名单，判断是否加入好友名单
                for (String account : removedAccounts) {
                    if (NIMClient.getService(FriendService.class).isMyFriend(account)) {
                        simplifyFriendSet.add(account);
                    }
                }

                // 通知观察者
                for (FriendDataChangedObserver o : friendObservers) {
                    o.onRemoveUserFromBlackList(removedAccounts);
                }
            }
        }
    };

    /**
     * ************************************ 单例 **********************************************
     */
    public static FriendDataCache getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        final static FriendDataCache instance = new FriendDataCache();
    }
}
