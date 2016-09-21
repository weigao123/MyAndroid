package com.garfield.weishu.contact;

import com.garfield.weishu.contact.item.AbsContactItem;
import com.garfield.weishu.contact.item.ContactItem;
import com.garfield.weishu.contact.item.FuncItem;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaowei3 on 2016/9/8.
 */
public class ContactDataProvider {
    private int[] itemTypes;
    private static List<AbsContactItem> funcData;

    public ContactDataProvider(int... itemTypes) {
        this.itemTypes = itemTypes;
    }

    public List<AbsContactItem> provide() {
        List<AbsContactItem> data = new ArrayList<>();

        for (int itemType : itemTypes) {
            data.addAll(provide(itemType));
        }

        return data;
    }

    private List<AbsContactItem> provide(int itemType) {
        switch (itemType) {
            case ItemTypes.FUNC:
                return generateFuncData();
            case ItemTypes.FRIEND:
                return generateFriendList();
            case ItemTypes.TEAM:
            case ItemTypes.TEAMS.ADVANCED_TEAM:
            case ItemTypes.TEAMS.NORMAL_TEAM:
            case ItemTypes.MSG:
            default:
                return new ArrayList<>();
        }
    }

    private List<AbsContactItem> generateFuncData() {
        if (funcData == null) {
            funcData = new ArrayList<>();
            funcData.add(new FuncItem(FuncItem.TYPE_NEW_FRIEND));
            funcData.add(new FuncItem(FuncItem.TYPE_ADVANCED_GROUP));
            funcData.add(new FuncItem(FuncItem.TYPE_DISCUSSION_GROUP));
            funcData.add(new FuncItem(FuncItem.TYPE_BLACK_LIST));
        }
        return funcData;
    }

    private List<AbsContactItem> generateFriendList() {
        List<NimUserInfo> nimUsers = UserInfoCache.getInstance().getUserInfoOfAllMyFriend();
        List<AbsContactItem> items = new ArrayList<>(nimUsers.size());
        for (NimUserInfo userInfo : nimUsers) {
            items.add(new ContactItem(userInfo.getAccount(), userInfo.getName(), ItemTypes.FRIEND));
        }
        return items;
    }

















}
