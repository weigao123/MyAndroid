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
import com.garfield.weishu.developer.JavaTest;
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
    protected String onGetToolbarTitle() {
        return getString(R.string.developer);
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        mData.add("1、SpeedView");
        mData.add("2、Sort Arithmetic");
        mData.add("3、Multi Thread");
        mData.add("4、SurfaceView");
        mData.add("5、FastBlur");
        mData.add("6、LayoutManager");
        mData.add("7、Plugin");

        //mData.add("15、音乐控制");
        //mData.add("16、fitsSystemWindows");
        if (JavaTest.on) {
            mData.add("100、Test Java");
        }

        DevelopAdapter adapter = new DevelopAdapter(getContext(), mData);
        adapter.setItemEventListener(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AppCache.getContext()));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(String item, int position) {
        switch (position + 1) {
            case 1:
                EventDispatcher.startFragmentEvent(new DeveloperSpeedFragment());
                break;
            case 2:
                EventDispatcher.startFragmentEvent(new DeveloperSortFragment());
                break;
            case 3:
                EventDispatcher.startFragmentEvent(new DeveloperThreadFragment());
                break;
            case 4:
                EventDispatcher.startFragmentEvent(new DeveloperSurfaceViewFragment());
                break;
            case 5:
                EventDispatcher.startFragmentEvent(new DeveloperBlurFragment());
                break;
            case 7:
                EventDispatcher.startFragmentEvent(new DeveloperPluginFragment());
                break;


            case 14:
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    EventDispatcher.startFragmentEvent(new MusicControlFragment());
                } else {
                    L.toast(R.string.system_not_support);
                }
            break;
            case 15:
                startActivity(new Intent(mActivity, DeveloperActivity.class));
                break;

            case 100:
                new JavaTest().doTest();
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
