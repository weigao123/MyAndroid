package com.garfield.weishu.developer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.garfield.baselib.adapter.DividerItemDecoration;
import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.base.recyclerview.TRecyclerAdapter;
import com.garfield.weishu.developer.music.ui.MusicControlFragment;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/11/23.
 */

public class DeveloperFragment extends AppBaseFragment implements TRecyclerAdapter.ItemEventListener<String> {

    @BindView(R.id.fragment_developer_recycler)
    RecyclerView mRecyclerView;

    private List<String> mData = new ArrayList<>();

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_developer;
    }

    @Override
    protected String onGetToolbarTitleResource() {
        return getString(R.string.developer);
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        mData.add("1、速度码表");
        mData.add("2、排序算法");
        mData.add("3、多线程");
        mData.add("4、SurfaceView");
        mData.add("5、音乐控制");
        //mData.add("6、fitsSystemWindows");
        DevelopAdapter adapter = new DevelopAdapter(getContext(), mData);
        adapter.setItemEventListener(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AppCache.getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL_LIST, true));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(String item, int position) {
        switch (position) {
            case 0:
                EventDispatcher.startFragmentEvent(new DeveloperSpeedFragment());
                break;
            case 1:
                EventDispatcher.startFragmentEvent(new DeveloperSortFragment());
                break;
            case 2:
                EventDispatcher.startFragmentEvent(new DeveloperThreadFragment());
                break;
            case 3:
                EventDispatcher.startFragmentEvent(new DeveloperSurfaceViewFragment());
                break;
            case 4:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    EventDispatcher.startFragmentEvent(new MusicControlFragment());
                } else {
                    L.toast(R.string.system_not_support);
                }
                break;
            case 5:
                startActivity(new Intent(mActivity, DeveloperActivity.class));
                break;
        }
    }

    @Override
    public void onItemLongPressed(String item, int position) {

    }


    private class DevelopAdapter extends TRecyclerAdapter<String> {

        private DevelopAdapter(Context context, List<String> items) {
            super(context, items);
        }

        @Override
        public Class getViewHolderClassAtPosition(int position) {
            return DeveloperListViewHolder.class;
        }
    }
}
