package com.garfield.weishu.contact;

import com.garfield.weishu.R;
import com.garfield.weishu.contact.item.AbsContactItem;
import com.garfield.weishu.contact.item.FuncItem;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;

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

    private void generateFriendList() {
        List<String> friends = NIMClient.getService(FriendService.class).getFriendAccounts();
    }

















}
