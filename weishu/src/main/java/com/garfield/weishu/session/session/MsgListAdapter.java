package com.garfield.weishu.session.session;

import android.content.Context;

import com.garfield.weishu.base.listview.TListAdapter;
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
    private MsgListEventListener eventListener;

    public MsgListAdapter(Context context, List items) {
        super(context, items);
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

    public void setEventListener(MsgListEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public MsgListEventListener getEventListener() {
        return eventListener;
    }

    @Override
    public Class getViewHolderClassAtPosition(int position) {
        return MsgViewHolderFactory.getViewHolderByType(getItems().get(position));
    }

    @Override
    public int getViewHolderCount() {
        return MsgViewHolderFactory.getViewTypeCount();
    }

    /**
     * 这两个属于所有ViewHolder公用的方法，拿出来写在Adapter里
     */
    public interface MsgListEventListener {
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
