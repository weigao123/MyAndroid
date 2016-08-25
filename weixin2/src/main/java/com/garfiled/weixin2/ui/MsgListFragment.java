package com.garfiled.weixin2.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.garfield.baselib.adapter.DividerItemDecoration;
import com.garfield.baselib.fragmentation.anim.DefaultHorizontalAnimator;
import com.garfield.baselib.fragmentation.anim.FragmentAnimator;
import com.garfield.baselib.utils.L;
import com.garfiled.weixin2.R;
import com.garfiled.weixin2.adapter.MsgListAdapter;
import com.garfiled.weixin2.adapter.OnItemClickListener;
import com.garfiled.weixin2.base.AppBaseFragment;
import com.garfiled.weixin2.bean.MsgListBean;
import com.garfiled.weixin2.event.StartBrotherEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by gaowei3 on 2016/8/1.
 */
public class MsgListFragment extends AppBaseFragment {

    private Random mRandom = new Random();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msglist, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView becyclerView = (RecyclerView) view.findViewById(R.id.msglist_recyclerview);
        becyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        // becyclerView.setItemAnimator(new DefaultItemAnimator());
        // 分隔线
        becyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));

        MsgListAdapter adapter = new MsgListAdapter(getMsgList(30));
        becyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                EventBus.getDefault().post(new StartBrotherEvent(new MsgFragment()));
            }
        });

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


}
