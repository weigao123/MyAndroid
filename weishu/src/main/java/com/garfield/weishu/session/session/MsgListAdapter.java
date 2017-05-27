package com.garfield.weishu.session.session;

import android.content.Context;

import com.garfield.weishu.base.listview.BaseListAdapter;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gwball on 2016/9/28.
 */

public class MsgListAdapter extends BaseListAdapter<IMMessage> {

    private String messageId;

    private MsgListEventListener eventListener;

    public MsgListAdapter(Context context, List items) {
        super(context, items);
        timedItems = new HashSet<>();
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

    public void deleteItem(IMMessage message, boolean isRelocateTime) {
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
            if (isRelocateTime) {
                relocateShowTimeItemAfterDelete(message, index);
            }
            notifyDataSetChanged();
        }
    }



    /*********************** 时间显示处理 ****************************/

    private Set<String> timedItems; // 需要显示消息时间的消息ID
    private IMMessage lastShowTimeItem; // 用于消息时间显示,判断和上条消息间的时间间隔

    public boolean needShowTime(IMMessage message) {
        return timedItems.contains(message.getUuid());
    }

    /**
     * 列表加入新消息时，更新时间显示
     */
    public void updateShowTimeItem(List<IMMessage> items, boolean fromStart, boolean update) {
        IMMessage anchor = fromStart ? null : lastShowTimeItem;
        for (IMMessage message : items) {
            if (setShowTimeFlag(message, anchor)) {
                anchor = message;
            }
        }

        if (update) {
            lastShowTimeItem = anchor;
        }
    }

    /**
     * 是否显示时间item
     */
    private boolean setShowTimeFlag(IMMessage message, IMMessage anchor) {
        boolean update = false;

        if (hideTimeAlways(message)) {
            setShowTime(message, false);
        } else {
            if (anchor == null) {
                setShowTime(message, true);
                update = true;
            } else {
                long time = anchor.getTime();
                long now = message.getTime();

                if (now - time == 0) {
                    // 消息撤回时使用
                    setShowTime(message, true);
                    lastShowTimeItem = message;
                    update = true;
                } else if (now - time < (long) (5 * 60 * 1000)) {
                    setShowTime(message, false);
                } else {
                    setShowTime(message, true);
                    update = true;
                }
            }
        }

        return update;
    }

    private void relocateShowTimeItemAfterDelete(IMMessage messageItem, int index) {
        // 如果被删的项显示了时间，需要继承
        if (needShowTime(messageItem)) {
            setShowTime(messageItem, false);
            if (getCount() > 0) {
                IMMessage nextItem;
                if (index == getCount()) {
                    //删除的是最后一项
                    nextItem = getItem(index - 1);
                } else {
                    //删除的不是最后一项
                    nextItem = getItem(index);
                }

                // 增加其他不需要显示时间的消息类型判断
                if (hideTimeAlways(nextItem)) {
                    setShowTime(nextItem, false);
                    if (lastShowTimeItem != null && lastShowTimeItem != null
                            && lastShowTimeItem.isTheSame(messageItem)) {
                        lastShowTimeItem = null;
                        for (int i = getCount() - 1; i >= 0; i--) {
                            IMMessage item = getItem(i);
                            if (needShowTime(item)) {
                                lastShowTimeItem = item;
                                break;
                            }
                        }
                    }
                } else {
                    setShowTime(nextItem, true);
                    if (lastShowTimeItem == null
                            || (lastShowTimeItem != null && lastShowTimeItem.isTheSame(messageItem))) {
                        lastShowTimeItem = nextItem;
                    }
                }
            } else {
                lastShowTimeItem = null;
            }
        }
    }

    private void setShowTime(IMMessage message, boolean show) {
        if (show) {
            timedItems.add(message.getUuid());
        } else {
            timedItems.remove(message.getUuid());
        }
    }

    private boolean hideTimeAlways(IMMessage message) {
        if (message.getSessionType() == SessionTypeEnum.ChatRoom) {
            return true;
        }
        switch (message.getMsgType()) {
            case notification:
                return true;
            default:
                return false;
        }
    }
}
