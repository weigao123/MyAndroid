package com.garfield.weishu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.garfield.baselib.ui.dialog.MenuDialog;
import com.garfield.baselib.utils.L;
import com.garfield.weishu.R;
import com.garfield.weishu.adapter.MsgListAdapter;
import com.garfield.weishu.adapter.OnItemClickListener;
import com.garfield.weishu.adapter.OnItemLongClickListener;
import com.garfield.weishu.bean.MsgListBean;
import com.garfield.weishu.config.UserPreferences;
import com.garfield.weishu.event.StartBrotherEvent;
import com.garfield.weishu.nim.NimInit;
import com.garfield.weishu.ui.activity.WelcomeActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/8/1.
 */
public class MsgListFragment extends AppBaseFragment {

    private Random mRandom = new Random();

    @BindView(R.id.network_status_bar)
    LinearLayout mNetworkStateBar;

    @BindView(R.id.status_desc_label)
    TextView mNetworkStatus;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_msg_list;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        RecyclerView becyclerView = (RecyclerView) rootView.findViewById(R.id.msglist_recyclerview);
        becyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        // becyclerView.setItemAnimator(new DefaultItemAnimator());
        // 分隔线，但是设置背景的时候无法把分割线包含
        // becyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));

        MsgListAdapter adapter = new MsgListAdapter(mActivity, getMsgList(30));
        becyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                EventBus.getDefault().post(new StartBrotherEvent(new MsgFragment()));
            }
        });
        adapter.setOnLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongPressed(int position, View view) {
//                new AlertDialog.Builder(mActivity)
//                        .setTitle("栈视图")
//                        .setView(contentView)
//                        .setPositiveButton("关闭", null)
//                        .setCancelable(true)
//                        .show();
                MenuDialog menuDialog = new MenuDialog();
                menuDialog.show(getChildFragmentManager(), "dialoglist");
            }
        });
        registerObservers(true);
    }

    private void registerObservers(boolean register) {
        //NIMClient.getService(AuthServiceObserver.class).observeOtherClients(clientsObserver, register);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }

    /**
     * 用户状态变化
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                kickOut(code);
            } else {
                L.d("NET STATUS: "+code);
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
        UserPreferences.saveUserToken("");
        if (code == StatusCode.PWD_ERROR) {
            L.d("user password error");
            Toast.makeText(getActivity(), R.string.login_failed, Toast.LENGTH_SHORT).show();
        } else {
            L.d("被踢下线");
        }
        onLogout();
    }

    private void onLogout() {
        // 清理缓存&注销监听&清除状态
        NimInit.logout();
        startActivity(new Intent(mActivity, WelcomeActivity.class));
        mActivity.finish();
    }

    private ArrayList<MsgListBean> getMsgList(int sum) {
        ArrayList<MsgListBean> msgList = new ArrayList<>();
        for (int i = 0; i < sum; i++) {
            MsgListBean msgListBean = new MsgListBean();
            msgListBean.setHeadImage(getRandomImageResource());
            msgListBean.setName(getRandomName());
            msgListBean.setContent(getRandomName());
            msgList.add(msgListBean);
        }
        return msgList;
    }

    private int getRandomImageResource() {
        switch (mRandom.nextInt(5)) {
            default:
            case 0:
                return R.drawable.cheese_1;
            case 1:
                return R.drawable.cheese_2;
            case 2:
                return R.drawable.cheese_3;
            case 3:
                return R.drawable.cheese_4;
            case 4:
                return R.drawable.cheese_5;
        }
    }
    private String getRandomName() {
        switch (mRandom.nextInt(5)) {
            default:
            case 0:
                return "中国最大，世界第一";
            case 1:
                return "中国最大，世界第一";
            case 2:
                return "中国最大，世界第一";
            case 3:
                return "中国最大，世界第一";
            case 4:
                return "中国最大，世界第一";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
    }
}
