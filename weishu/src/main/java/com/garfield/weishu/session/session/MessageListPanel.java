package com.garfield.weishu.session.session;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.garfield.baselib.adapter.DividerItemDecoration;
import com.garfield.weishu.R;
import com.garfield.weishu.base.listview.TListViewHolder;
import com.garfield.weishu.base.listview.AutoRefreshListView;
import com.garfield.weishu.base.listview.ListViewUtil;
import com.garfield.weishu.utils.ClipboardUtil;
import com.garfield.weishu.utils.ScreenUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaowei3 on 2016/9/26.
 */

public class MessageListPanel {
    private List<IMMessage> items = new ArrayList<>();

    @BindView(R.id.fragment_session_list)
    MessageListView messageListView;
    private MsgListAdapter adapter;
    private View rootView;
    private ModuleProxy moduleProxy;

    private SessionTypeEnum sessionType = SessionTypeEnum.P2P;
    private String account;
    private Handler uiHandler = new Handler();

    public MessageListPanel(View rootView, String account, ModuleProxy moduleProxy) {
        ButterKnife.bind(this, rootView);
        this.rootView = rootView;
        this.account = account;
        this.moduleProxy = moduleProxy;
        init();
    }

    private void init() {
        messageListView.setMode(AutoRefreshListView.Mode.FRONT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            messageListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        adapter = new MsgListAdapter(rootView.getContext(), items);
        adapter.setEventListener(new HolderEventListener());
        messageListView.setAdapter(adapter);
        messageListView.setOnRefreshListener(new MessageLoader(null));
        messageListView.setListViewEventListener(new MessageListView.OnListViewEventListener() {
            @Override
            public void onListViewStartScroll() {
                moduleProxy.shouldCollapseInputPanel();
            }

            @Override
            public void onSizeChanged() {
                ListViewUtil.scrollToBottom(messageListView);
            }
        });

        registerObservers(true);
    }

    public void onIncomingMessage(List<IMMessage> messages) {
        boolean needScrollToBottom = ListViewUtil.isLastMessageVisible(messageListView);
        boolean needRefresh = false;
        List<IMMessage> addedListItems = new ArrayList<>(messages.size());
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
        IMMessage lastMsg = messages.get(messages.size() - 1);
        if (isMyMessage(lastMsg)) {
            if (needScrollToBottom) {
                ListViewUtil.scrollToBottom(messageListView);
            }
        }
    }

    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == sessionType
                && message.getSessionId() != null
                && message.getSessionId().equals(account);
    }

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeMsgStatus(messageStatusObserver, register);
    }

    private Observer<IMMessage> messageStatusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            if (isMyMessage(message)) {
                onMessageStatusChange(message);
            }
        }
    };

    private void onMessageStatusChange(IMMessage message) {
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            item.setStatus(message.getStatus());
            refreshViewHolderByIndex(index);
        }
    }

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            IMMessage message = items.get(i);
            if (TextUtils.equals(message.getUuid(), uuid)) {
                return i;
            }
        }

        return -1;
    }

    private void refreshViewHolderByIndex(final int index) {
        rootView.post(new Runnable() {

            @Override
            public void run() {
                if (index < 0) {
                    return;
                }

                Object tag = ListViewUtil.getViewHolderByIndex(messageListView, index);
                if (tag instanceof MsgListViewHolderBase) {
                    MsgListViewHolderBase viewHolder = (MsgListViewHolderBase) tag;
                    viewHolder.refreshCurrentItem();
                }
            }
        });
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

    private class MessageLoader implements AutoRefreshListView.OnRefreshListener {

        private static final int LOAD_MESSAGE_COUNT = 20;

        private QueryDirectionEnum direction = null;

        private IMMessage anchor;

        private boolean firstLoad = true;

        public MessageLoader(IMMessage anchor) {
            this.anchor = anchor;
            if (anchor == null) {
                loadFromLocal(QueryDirectionEnum.QUERY_OLD);
            } else {
                loadAnchorContext();
            }
        }

        private RequestCallback<List<IMMessage>> callback = new RequestCallbackWrapper<List<IMMessage>>() {
            @Override
            public void onResult(int code, List<IMMessage> messages, Throwable exception) {
                if (messages != null) {
                    onMessageLoaded(messages);
                }
            }
        };

        private void loadFromLocal(QueryDirectionEnum direction) {
            this.direction = direction;
            messageListView.onRefreshStart(direction == QueryDirectionEnum.QUERY_NEW ? AutoRefreshListView.Mode.END : AutoRefreshListView.Mode.FRONT);
            NIMClient.getService(MsgService.class).queryMessageListEx(anchor(), direction, LOAD_MESSAGE_COUNT, true)
                    .setCallback(callback);
        }

        private void loadAnchorContext() {
            // query old
            this.direction = QueryDirectionEnum.QUERY_OLD;
            messageListView.onRefreshStart(AutoRefreshListView.Mode.FRONT);
            NIMClient.getService(MsgService.class).queryMessageListEx(anchor(), direction, LOAD_MESSAGE_COUNT, true)
                    .setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
                        @Override
                        public void onResult(int code, List<IMMessage> messages, Throwable exception) {
                            if (code != ResponseCode.RES_SUCCESS || exception != null) {
                                return;
                            }
                            onMessageLoaded(messages);

                            // query new
                            direction = QueryDirectionEnum.QUERY_NEW;
                            messageListView.onRefreshStart(AutoRefreshListView.Mode.END);
                            NIMClient.getService(MsgService.class).queryMessageListEx(anchor(), direction, LOAD_MESSAGE_COUNT, true)
                                    .setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
                                        @Override
                                        public void onResult(int code, List<IMMessage> messages, Throwable exception) {
                                            if (code != ResponseCode.RES_SUCCESS || exception != null) {
                                                return;
                                            }
                                            onMessageLoaded(messages);
                                            // scroll to position
                                            scrollToAnchor(anchor);
                                        }
                                    });
                        }
                    });
        }

        private IMMessage anchor() {
            if (items.size() == 0) {
                return anchor == null ? MessageBuilder.createEmptyMessage(account, sessionType, 0) : anchor;
            } else {
                int index = (direction == QueryDirectionEnum.QUERY_NEW ? items.size() - 1 : 0);
                return items.get(index);
            }
        }

        /**
         * 历史消息加载处理
         *
         * @param messages
         */
        private void onMessageLoaded(List<IMMessage> messages) {
            int count = messages.size();

            if (firstLoad && items.size() > 0) {
                // 在第一次加载的过程中又收到了新消息，做一下去重
                for (IMMessage message : messages) {
                    for (IMMessage item : items) {
                        if (item.isTheSame(message)) {
                            items.remove(item);
                            break;
                        }
                    }
                }
            }

            if (firstLoad && anchor != null) {
                items.add(anchor);
            }

            List<IMMessage> result = new ArrayList<>();
            for (IMMessage message : messages) {
                result.add(message);
            }
            if (direction == QueryDirectionEnum.QUERY_NEW) {
                items.addAll(result);
            } else {
                items.addAll(0, result);
            }

            // 如果是第一次加载，updateShowTimeItem返回的就是lastShowTimeItem
            if (firstLoad) {
                ListViewUtil.scrollToBottom(messageListView);
                //sendReceipt(); // 发送已读回执
            }

            //adapter.updateShowTimeItem(items, true, firstLoad);
            //updateReceipt(items); // 更新已读回执标签

            // 必须先notify，才能使listView.getCount()同步
            adapter.notifyDataSetChanged();
            messageListView.onRefreshComplete(count, LOAD_MESSAGE_COUNT, true);

            firstLoad = false;
        }

        @Override
        public void onRefreshFromStart() {
            loadFromLocal(QueryDirectionEnum.QUERY_OLD);
        }

        @Override
        public void onRefreshFromEnd() {
            loadFromLocal(QueryDirectionEnum.QUERY_NEW);
        }
    }

    public void scrollToAnchor(final IMMessage anchor) {
        if (anchor == null) {
            return;
        }

        int position = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getUuid().equals(anchor.getUuid())) {
                position = i;
                break;
            }
        }

        if (position >= 0) {
            final int jumpTo = position == 0 ? 0 : position + messageListView.getHeaderViewsCount();
            uiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ListViewUtil.scrollToPosition(messageListView, jumpTo, jumpTo == 0 ? 0 : ScreenUtil.dip2px(30));
                }
            }, 30);
        }
    }

    // 刷新消息列表
    public void refreshMessageList() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void onMessageSend(IMMessage message) {
        items.add(message);
        List<IMMessage> addedListItems = new ArrayList<>(1);
        addedListItems.add(message);
        //adapter.updateShowTimeItem(addedListItems, false, true);

        adapter.notifyDataSetChanged();
        ListViewUtil.scrollToBottom(messageListView);
    }

    private class HolderEventListener implements MsgListAdapter.MsgListEventListener {

        @Override
        public boolean onViewHolderLongClick(final IMMessage item) {
            MaterialDialog dialog = new MaterialDialog.Builder(rootView.getContext())
                    .items(R.array.message_menu)
                    .listSelector(R.drawable.bg_press_gray)
                    .itemsColorRes(R.color.black)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                            switch (position) {
                                case 0:
                                    ClipboardUtil.clipboardCopyText(rootView.getContext(), item.getContent());
                                    break;
                                case 1:
                                    deleteItem(item);
                                    break;
                            }
                        }
                    })
                    .build();
            RecyclerView recyclerView = dialog.getRecyclerView();
            recyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL_LIST));
            dialog.show();
            return false;
        }

        @Override
        public void onFailedBtnClick(final IMMessage message) {
            MaterialDialog dialog = new MaterialDialog.Builder(rootView.getContext())
                    .title(R.string.is_resent)
                    .positiveText(R.string.confirm)
                    .positiveColorRes(R.color.colorPrimary)
                    .negativeText(R.string.cancel)
                    .negativeColorRes(R.color.colorPrimary)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (message.getDirect() == MsgDirectionEnum.Out) {
                                if (message.getStatus() == MsgStatusEnum.fail) {
                                    resendMessage(message);
                                }
                            }
                        }
                    })
                    .build();
            dialog.show();

        }
    }

    private void resendMessage(IMMessage message) {
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            item.setStatus(MsgStatusEnum.sending);
            refreshViewHolderByIndex(index);
        }

        NIMClient.getService(MsgService.class).sendMessage(message, true);
    }

    private void deleteItem(IMMessage item) {
        NIMClient.getService(MsgService.class).deleteChattingHistory(item);
        adapter.deleteItem(item);
    }

    public void onDestoryView() {
        registerObservers(false);
    }
}
