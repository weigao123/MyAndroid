package com.garfield.weishu.session.sessionlist;

import android.content.Context;

import com.garfield.weishu.base.recyclerview.TRecyclerAdapter;
import com.garfield.weishu.base.recyclerview.TRecyclerViewHolder;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

/**
 * Created by gwball on 2016/8/3.
 */
public class SessionListAdapter extends TRecyclerAdapter<RecentContact> {

//    private SessionListEventListener mEventListener;

    public SessionListAdapter(Context context, List<RecentContact> data) {
        super(context, data);
    }

    @Override
    public Class getViewHolderClassAtPosition(int position) {
        return SessionListViewHolder.class;
    }

//    public void setEventListener(SessionListEventListener eventListener) {
//        this.mEventListener = eventListener;
//    }
//
//    public SessionListEventListener getItemEventListener() {
//        return mEventListener;
//    }
//
//    public interface SessionListEventListener {
//        void onItemClick(RecentContact account);
//        void onItemLongPressed(RecentContact contact);
//    }
}
