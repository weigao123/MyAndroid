package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.garfield.baselib.ui.widget.LetterIndexView;
import com.garfield.baselib.utils.L;
import com.garfield.weishu.R;
import com.garfield.weishu.contact.ContactDataAdapter;
import com.garfield.weishu.contact.ContactDataProvider;
import com.garfield.weishu.contact.ItemTypes;
import com.garfield.weishu.contact.item.AbsContactItem;
import com.garfield.weishu.contact.item.ContactItem;
import com.garfield.weishu.contact.item.FuncItem;
import com.garfield.weishu.contact.model.ContactGroupStrategy;
import com.garfield.weishu.contact.viewholder.ContactHolder;
import com.garfield.weishu.contact.viewholder.FuncHolder;
import com.garfield.weishu.contact.viewholder.LabelHolder;
import com.garfield.weishu.event.StartBrotherEvent;
import com.garfield.weishu.nim.FriendDataCache;
import com.garfield.weishu.nim.LoginSyncDataStatusObserver;
import com.netease.nimlib.sdk.Observer;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/8/1.
 */
public class ContactFragment extends AppBaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnLongClickListener {

    @BindView(R.id.contact_list_view)
    ListView mListView;

    private ContactDataAdapter adapter;


    @BindView(R.id.contact_letter_index)
    LetterIndexView mLetterIndexView;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_contact_list;
    }

    private static final class ContactsGroupStrategy extends ContactGroupStrategy {
        public ContactsGroupStrategy() {
            add(ContactGroupStrategy.GROUP_NULL, -1, "");
            addABC(0);
        }
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        ContactDataProvider dataProvider = new ContactDataProvider(ItemTypes.FUNC, ItemTypes.FRIEND);

        adapter = new ContactDataAdapter(mActivity, new ContactsGroupStrategy(), dataProvider);
        adapter.addViewHolder(ItemTypes.LABEL, LabelHolder.class);
        adapter.addViewHolder(ItemTypes.FUNC, FuncHolder.class);
        adapter.addViewHolder(ItemTypes.FRIEND, ContactHolder.class);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnLongClickListener(this);

        registerObserver(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reload(false);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AbsContactItem item = (AbsContactItem) adapter.getItem(position);
        if (item == null) {
            return;
        }
        int type = item.getItemType();
        if (type == ItemTypes.FUNC && item instanceof FuncItem) {
            switch (((FuncItem) item).getFuncType()) {
                case FuncItem.TYPE_NEW_FRIEND:
                    EventBus.getDefault().post(new StartBrotherEvent(new SearchUserFragment()));
                    break;
            }
            return;
        }

        if (type == ItemTypes.FRIEND && item instanceof ContactItem) {

            return;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }


    /**
     * 加载通讯录数据并刷新
     *
     * @param reload true则重新加载数据；false则判断当前数据源是否空，若空则重新加载，不空则不加载
     */
    private void reload(boolean reload) {
        // 开始加载
        if (!adapter.load(reload)) {
            // 如果不需要加载，则直接当完成处理
            //onReloadCompleted();
        }
    }




    private void registerObserver(boolean register) {
        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
    }

    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            reloadWhenDataChanged(accounts, "onAddedOrUpdatedFriends", true);
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            reloadWhenDataChanged(accounts, "onDeletedFriends", true);
        }

        @Override
        public void onAddUserToBlackList(List<String> accounts) {
            reloadWhenDataChanged(accounts, "onAddUserToBlackList", true);
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> accounts) {
            reloadWhenDataChanged(accounts, "onRemoveUserFromBlackList", true);
        }
    };

    private void reloadWhenDataChanged(List<String> accounts, String reason, boolean reload) {
        reloadWhenDataChanged(accounts, reason, reload, true);
    }

    // force是false表示非好友，不刷新，true表示刷新
    private void reloadWhenDataChanged(List<String> accounts, String reason, boolean reload, boolean force) {
        if (accounts == null || accounts.isEmpty()) {
            return;
        }

        boolean needReload = false;
        if(!force) {
            // 非force：与通讯录无关的（非好友）变更通知，去掉
            for (String account : accounts) {
                if (FriendDataCache.getInstance().isMyFriend(account)) {
                    needReload = true;
                    break;
                }
            }
        } else{
            needReload = true;
        }

        if (!needReload) {
            L.d("no need to reload contact");
            return;
        }

        // log
        StringBuilder sb = new StringBuilder();
        sb.append("ContactFragment received data changed as [" + reason + "] : ");
        if (accounts != null && !accounts.isEmpty()) {
            for (String account : accounts) {
                sb.append(account);
                sb.append(" ");
            }
            sb.append(", changed size=" + accounts.size());
        }
        L.d(sb.toString());

        // reload
        reload(reload);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObserver(false);
    }
}
