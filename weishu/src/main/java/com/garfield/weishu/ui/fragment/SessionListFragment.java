package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.garfield.baselib.ui.dialog.EasyMenuDialog;
import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.session.SessionListAdapter;
import com.garfield.weishu.base.adapter.OnItemClickListener;
import com.garfield.weishu.base.adapter.OnItemLongClickListener;
import com.garfield.weishu.nim.RegisterAndLogin;
import com.garfield.weishu.nim.cache.FriendDataCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/8/1.
 */
public class SessionListFragment extends AppBaseFragment {
    public static final long RECENT_TAG_STICKY = 1; // 联系人置顶tag

    @BindView(R.id.network_status_bar)
    LinearLayout mNetworkStateBar;

    @BindView(R.id.status_desc_label)
    TextView mNetworkStatus;

    @BindView(R.id.fragment_session_list_no_session)
    LinearLayout mNoSessionRecord;

    private List<RecentContact> items;
    private SessionListAdapter adapter;
    private boolean msgLoaded = false;
    private List<RecentContact> loadedRecents;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_session_list;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        RecyclerView becyclerView = (RecyclerView) rootView.findViewById(R.id.session_list_recyclerview);
        becyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        // 动画
        // becyclerView.setItemAnimator(new DefaultItemAnimator());
        // 分隔线，与item分离，设置背景的时候无法把分割线包含
        // becyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));

        items = new ArrayList<>();
        adapter = new SessionListAdapter(mActivity, items);
        becyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, Object object) {
                EventDispatcher.getFragmentJumpEvent().onShowSession((String)object);
            }
        });
        adapter.setOnLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongPressed(int position, View view) {
                EasyMenuDialog menuDialog = new EasyMenuDialog();
                menuDialog.show(getChildFragmentManager(), "dialoglist");
            }
        });
        registerObservers(true);
        requestMessages(true);
    }

    private void requestMessages(boolean delay) {
        if (msgLoaded) {
            return;
        }
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (msgLoaded) {
                    return;
                }
                // 查询最近联系人列表数据
                NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                        if (code != ResponseCode.RES_SUCCESS || recents == null) {
                            return;
                        }
                        loadedRecents = recents;
                        msgLoaded = true;
                        if (isAdded()) {
                            onRecentContactsLoaded();
                        }
                    }
                });
            }
        }, delay ? 250 : 0);
    }

    private void onRecentContactsLoaded() {
        items.clear();
        if (loadedRecents != null) {
            items.addAll(loadedRecents);
            loadedRecents = null;
        }
        refreshMessages(true);
    }

    private void registerObservers(boolean register) {
        //NIMClient.getService(AuthServiceObserver.class).observeOtherClients(clientsObserver, register);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);

        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeRecentContact(messageObserver, register);
        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);

    }

    private Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> messages) {
            int index;
            for (RecentContact msg : messages) {
                index = -1;
                for (int i = 0; i < items.size(); i++) {
                    if (msg.getContactId().equals(items.get(i).getContactId())
                            && msg.getSessionType() == (items.get(i).getSessionType())) {
                        index = i;
                        break;
                    }
                }
                if (index >= 0) {
                    items.remove(index);
                }
                items.add(msg);
            }
            refreshMessages(true);
        }
    };


    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            refreshMessages(false);
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            refreshMessages(false);
        }
    };

    private void refreshMessages(boolean unreadChanged) {
        sortRecentContacts(items);
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
        boolean empty = items.isEmpty() && msgLoaded;
        mNoSessionRecord.setVisibility(empty ? View.VISIBLE : View.GONE);
    }


    /**
     * **************************** 排序 ***********************************
     */
    private void sortRecentContacts(List<RecentContact> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<RecentContact> comp = new Comparator<RecentContact>() {

        @Override
        public int compare(RecentContact o1, RecentContact o2) {
            // 先比较置顶tag
            long sticky = (o1.getTag() & RECENT_TAG_STICKY) - (o2.getTag() & RECENT_TAG_STICKY);
            if (sticky != 0) {
                return sticky > 0 ? -1 : 1;
            } else {
                long time = o1.getTime() - o2.getTime();
                return time == 0 ? 0 : (time > 0 ? -1 : 1);
            }
        }
    };

    /**
     * 用户状态变化
     */
    private Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                kickOut(code);
            } else {
                if (code == StatusCode.NET_BROKEN) {
                    mNetworkStateBar.setVisibility(View.VISIBLE);
                    mNetworkStatus.setText(R.string.status_network_is_not_available);
                } else if (code == StatusCode.UNLOGIN) {
                    mNetworkStateBar.setVisibility(View.VISIBLE);
                    mNetworkStatus.setText(R.string.status_unlogin);
                } else if (code == StatusCode.CONNECTING) {
                    mNetworkStateBar.setVisibility(View.VISIBLE);
                    mNetworkStatus.setText(R.string.status_connecting);
                } else if (code == StatusCode.LOGINING) {
                    mNetworkStateBar.setVisibility(View.VISIBLE);
                    mNetworkStatus.setText(R.string.status_logining);
                } else {
                    mNetworkStateBar.setVisibility(View.GONE);
                }
            }
        }
    };

    private void kickOut(StatusCode code) {
        if (code == StatusCode.PWD_ERROR) {
            Toast.makeText(getActivity(), R.string.login_account_or_password_wrong, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.kicked_out, Toast.LENGTH_SHORT).show();
        }
        RegisterAndLogin.logout(mActivity);
    }

    private void enableMsgNotification(boolean enable) {
        if (enable) {
            // {MSG_CHATTING_ACCOUNT_NONE} 目前没有与任何人对话，需要状态栏消息通知
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        } else {
            // {MSG_CHATTING_ACCOUNT_ALL} 目前没有与任何人对话，但能看到消息提醒（比如在消息列表界面），不需要在状态栏做消息通知
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
    }
}
