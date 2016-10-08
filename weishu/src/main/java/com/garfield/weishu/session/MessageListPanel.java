package com.garfield.weishu.session;

import android.view.View;

import com.garfield.weishu.base.adapter.TAdapterDelegate;
import com.garfield.weishu.base.adapter.TViewHolder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by gaowei3 on 2016/9/26.
 */

public class MessageListPanel implements TAdapterDelegate {
    private List<IMMessage> items;
    private MsgAdapter adapter;
    private View rootView;

    private SessionTypeEnum sessionTypeEnum = SessionTypeEnum.P2P;
    private String account;

    public MessageListPanel(View rootView) {
        ButterKnife.bind(this, rootView);
        init();
    }

    private void init() {
        items = new ArrayList<>();
        adapter = new MsgAdapter(rootView.getContext(), items, this);
    }

    public void onIncomingMessage(List<IMMessage> messages) {
        List<IMMessage> addedListItems = new ArrayList<>(messages.size());
        boolean needRefresh = false;
        for (IMMessage message : messages) {
            if (isMyMessage(message)) {
                items.add(message);
                addedListItems.add(message);
                needRefresh = true;
            }
        }
        if (needRefresh) {
            sortMessages(items);
            adapter.notifyDataSetChanged();
        }

    }

    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == sessionTypeEnum
                && message.getSessionId() != null
                && message.getSessionId().equals(account);
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public Class<? extends TViewHolder> getViewHolderClassAtPosition(int position) {
        return null;
    }

    @Override
    public boolean enabled(int position) {
        return false;
    }

    /**
     * **************************** 排序 ***********************************
     */
    private void sortMessages(List<IMMessage> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<IMMessage> comp = new Comparator<IMMessage>() {

        @Override
        public int compare(IMMessage o1, IMMessage o2) {
            long time = o1.getTime() - o2.getTime();
            return time == 0 ? 0 : (time < 0 ? -1 : 1);
        }
    };
}
