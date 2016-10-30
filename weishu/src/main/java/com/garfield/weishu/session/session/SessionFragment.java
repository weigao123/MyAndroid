package com.garfield.weishu.session.session;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;

import com.garfield.weishu.R;
import com.garfield.weishu.nim.NimConfig;
import com.garfield.weishu.ui.fragment.AppBaseFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;

import java.util.List;

import static com.garfield.weishu.AppCache.USER_ACCOUNT;

/**
 * Created by gaowei3 on 2016/8/4.
 */
public class SessionFragment extends AppBaseFragment implements ModuleProxy {

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

        messageListPanel = new MessageListPanel(rootView, mAccount, this);
        mInputPanel = new InputPanel(rootView, mAccount, this);
        registerObservers(true);
    }

    public static SessionFragment newInstance(String account) {
        Bundle args = new Bundle();
        args.putString(USER_ACCOUNT, account);
        SessionFragment fragment = new SessionFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);
//        if (transit == FragmentTransaction.TRANSIT_FRAGMENT_OPEN && enter && animation.getDuration() > 100) {
//            animation.setStartOffset(300);
//        }
//        return animation;
//    }

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
    public boolean sendMessage(IMMessage msg) {
        NIMClient.getService(MsgService.class).sendMessage(msg, false).setCallback(new RequestCallbackWrapper<Void>() {
            @Override
            public void onResult(int i, Void aVoid, Throwable throwable) {
                if (i == ResponseCode.RES_SUCCESS) {
                    //Toast.makeText(AppCache.getContext(), R.string.send_success, Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(AppCache.getContext(), R.string.send_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });
        messageListPanel.onMessageSend(msg);
        return true;
    }

    @Override
    public void shouldCollapseInputPanel() {
        mInputPanel.collapse(false);
    }

    @Override
    public boolean isLongClickEnabled() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        messageListPanel.onDestoryView();
        registerObservers(false);
    }

}
