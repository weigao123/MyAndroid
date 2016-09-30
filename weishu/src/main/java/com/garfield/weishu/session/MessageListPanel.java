package com.garfield.weishu.session;

import android.view.View;

import com.garfield.weishu.base.adapter.TAdapterDelegate;
import com.garfield.weishu.base.adapter.TViewHolder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by gaowei3 on 2016/9/26.
 */

public class MessageListPanel implements TAdapterDelegate {
    private List<IMMessage> items;
    private MsgAdapter adapter;
    private View rootView;

    public MessageListPanel(View rootView) {
        ButterKnife.bind(this, rootView);
        init();
    }

    private void init() {
        items = new ArrayList<>();
        adapter = new MsgAdapter(rootView.getContext(), items, this);
    }

    public void onIncomingMessage(List<IMMessage> messages) {


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
}
