package com.garfield.weishu.session;

import android.content.Context;

import com.garfield.weishu.base.listview.TListAdapter;
import com.garfield.weishu.base.listview.TListAdapterDelegate;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gwball on 2016/9/28.
 */

public class MsgListAdapter extends TListAdapter<IMMessage> {

    private String messageId;
    private Set<String> timedItems; // 需要显示消息时间的消息ID
    private IMMessage lastShowTimeItem; // 用于消息时间显示,判断和上条消息间的时间间隔
    private ViewHolderEventListener eventListener;

    public MsgListAdapter(Context context, List items, TListAdapterDelegate delegate) {
        super(context, items, delegate);
        timedItems = new HashSet<>();
    }

    public boolean needShowTime(IMMessage message) {
        return timedItems.contains(message.getUuid());
    }


    public void setUuid(String messageId) {
        this.messageId = messageId;
    }

    public String getUuid() {
        return messageId;
    }

    public void setEventListener(ViewHolderEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public ViewHolderEventListener getEventListener() {
        return eventListener;
    }

    /**
     * 这两个属于所有ViewHolder公用的方法，拿出来写在Adapter里
     */
    public interface ViewHolderEventListener {
        boolean onViewHolderLongClick(IMMessage item);
        void onFailedBtnClick(IMMessage resendMessage);
    }

    public void deleteItem(IMMessage message) {
        if (message == null) {
            return;
        }
        int index = 0;
        // 遍历一遍，找到对应的item
        for (IMMessage item : getItems()) {
            if (item.isTheSame(message)) {
                break;
            }
            ++index;
        }
        if (index < getCount()) {
            getItems().remove(index);
            notifyDataSetChanged();
        }
    }
}
