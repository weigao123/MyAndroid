package com.garfield.weishu.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.garfield.weishu.R;
import com.garfield.weishu.bean.ContactBean;
import com.garfield.weishu.session.InputPanel;
import com.garfield.weishu.news.NewsAdapter;
import com.garfield.weishu.session.MessageListPanel;
import com.garfield.weishu.session.listview.MessageListView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.garfield.weishu.AppCache.USER_ACCOUNT;

/**
 * Created by gaowei3 on 2016/8/4.
 */
public class SessionFragment extends AppBaseFragment {

    private String mAccount;
    private InputPanel mInputPanel;
    protected MessageListPanel messageListPanel;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_session;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mAccount = getArguments().getString(USER_ACCOUNT);

        messageListPanel = new MessageListPanel(rootView, mAccount);
        mInputPanel = new InputPanel(rootView, mAccount);
        registerObservers(true);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static SessionFragment newInstance(String account) {
        Bundle args = new Bundle();
        args.putString(USER_ACCOUNT, account);
        SessionFragment fragment = new SessionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(incomingMessageObserver, register);
        service.observeMessageReceipt(messageReceiptObserver, register);
    }


    /**
     * 消息接收观察者
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }

            messageListPanel.onIncomingMessage(messages);
            //sendMsgReceipt(); // 发送已读回执
        }
    };

    private Observer<List<MessageReceipt>> messageReceiptObserver = new Observer<List<MessageReceipt>>() {
        @Override
        public void onEvent(List<MessageReceipt> messageReceipts) {
            //receiveReceipt();
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        //messageListPanel.onDestroy();
        registerObservers(false);
    }
}
