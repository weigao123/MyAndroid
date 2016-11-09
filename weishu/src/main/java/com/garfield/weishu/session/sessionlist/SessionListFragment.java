package com.garfield.weishu.session.sessionlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.garfield.baselib.adapter.DividerItemDecoration;
import com.garfield.baselib.utils.L;
import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.base.recyclerview.RecyclerViewHolder;
import com.garfield.weishu.base.recyclerview.TRecyclerAdapter;
import com.garfield.weishu.nim.NimConfig;
import com.garfield.weishu.nim.cache.UserInfoCache;
import com.garfield.weishu.nim.RegisterAndLogin;
import com.garfield.weishu.nim.cache.FriendDataCache;
import com.garfield.weishu.ui.fragment.AppBaseFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
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
    private RecyclerView recyclerView;
    private SessionListAdapter adapter;
    private boolean msgLoaded = false;
    private List<RecentContact> loadedRecents;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_session_list;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.session_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        // 动画
        // recyclerView.setItemAnimator(new DefaultItemAnimator());
        // 分隔线，与item分离，设置背景的时候无法把分割线包含
        // recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));

        items = new ArrayList<>();
        adapter = new SessionListAdapter(mActivity, items);
        recyclerView.setAdapter(adapter);
        adapter.setItemEventListener(new TRecyclerAdapter.ItemEventListener<RecentContact>() {
            @Override
            public void onItemClick(final RecentContact recentContact) {
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventDispatcher.getFragmentJumpEvent().onShowSession(recentContact.getContactId());
                    }
                }, 50);
            }

            @Override
            public void onItemLongPressed(final RecentContact recent) {
                MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                        .items(isTagSet(recent, RECENT_TAG_STICKY)? R.array.session_menu2 : R.array.session_menu1)
                        .listSelector(R.drawable.bg_press_gray)
                        .itemsColorRes(R.color.black)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                if (TextUtils.equals(text, getResources().getText(R.string.stick_top_chat))) {
                                    addTag(recent, RECENT_TAG_STICKY);
                                    NIMClient.getService(MsgService.class).updateRecent(recent);
                                    refreshMessages(true);
                                }
                                if (TextUtils.equals(text, getResources().getText(R.string.cancel_stick_top_chat))) {
                                    removeTag(recent, RECENT_TAG_STICKY);
                                    NIMClient.getService(MsgService.class).updateRecent(recent);
                                    refreshMessages(true);
                                }
                                if (TextUtils.equals(text, getResources().getText(R.string.delete_chat))) {
                                    NIMClient.getService(MsgService.class).deleteRecentContact(recent);
                                    NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId(), recent.getSessionType());
                                    items.remove(recent);
                                    refreshMessages(false);
                                }
                            }
                        })
                        .build();
                RecyclerView recyclerView = dialog.getRecyclerView();
                recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));
                dialog.show();
                //EasyMenuDialog menuDialog = new EasyMenuDialog();
                //menuDialog.show(getChildFragmentManager(), "dialoglist");
            }
        });

        registerObservers(true);
        requestMessages(true);
    }

    private void addTag(RecentContact recent, long tag) {
        tag = recent.getTag() | tag;
        recent.setTag(tag);
    }

    private void removeTag(RecentContact recent, long tag) {
        tag = recent.getTag() & ~tag;
        recent.setTag(tag);
    }

    private boolean isTagSet(RecentContact recent, long tag) {
        return (recent.getTag() & tag) == tag;
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
        service.observeRecentContact(sessionObserver, register);
        service.observeMsgStatus(statusObserver, register);
        service.observeRecentContactDeleted(deleteObserver, register);
        //service.observeRevokeMessage(revokeMessageObserver, register);

        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
        UserInfoCache.getInstance().registerUserInfoChangedObserver(userInfoChangedObserver, register);
    }

    /**
     * 只要会话创建或开始发送(中)就回调，但是发送成功后并不回调
     */
    private Observer<List<RecentContact>> sessionObserver = new Observer<List<RecentContact>>() {
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
                // 旧的替换成新的
                if (index >= 0) {
                    items.remove(index);
                }
                items.add(msg);
            }
            refreshMessages(true);
        }
    };

    /**
     * 需要发送成功后主动改recent状态
     */
    private Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            int index = -1;
            for (int i = 0; i < items.size(); i++) {
                RecentContact item = items.get(i);
                if (TextUtils.equals(item.getRecentMessageId(), message.getUuid())) {
                    index = i;
                }
            }
            if (index >= 0 && index < items.size()) {
                RecentContact item = items.get(index);
                item.setMsgStatus(message.getStatus());
                refreshViewHolderByIndex(index);
            }
        }
    };

    private void refreshViewHolderByIndex(final int index) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RecyclerViewHolder viewHolder = (RecyclerViewHolder) recyclerView.findViewHolderForLayoutPosition(index);
                if (viewHolder != null) {
                    viewHolder.refresh();
                }
            }
        });
    }

    /**
     * 删除一个会话，如果是null就是清空，在清空数据库时会回调
     */
    private Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {
        @Override
        public void onEvent(RecentContact recentContact) {
            if (recentContact != null) {
                for (RecentContact item : items) {
                    if (TextUtils.equals(item.getContactId(), recentContact.getContactId())
                            && item.getSessionType() == recentContact.getSessionType()) {
                        items.remove(item);
                        refreshMessages(true);
                        break;
                    }
                }
            } else {
                items.clear();
                refreshMessages(true);
            }
        }
    };

    /**
     * 不知道干啥用
     */
    private FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
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

    private UserInfoCache.UserInfoChangedObserver userInfoChangedObserver = new UserInfoCache.UserInfoChangedObserver() {
        @Override
        public void onUserInfoChanged(List<String> accounts) {
            refreshMessages(false);
        }
    };

    private void refreshMessages(boolean sort) {
        if (sort) {
            sortRecentContacts(items);
        }
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


    @Override
    public void onResume() {
        super.onResume();
        NimConfig.nofityWithNoTopBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
    }
}
