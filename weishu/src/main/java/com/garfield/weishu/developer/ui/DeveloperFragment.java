package com.garfield.weishu.developer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.garfield.baselib.utils.system.L;
import com.garfield.baselib.utils.system.ScreenUtil;
import com.garfield.weishu.BuildConfig;
import com.garfield.weishu.R;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.base.recyclerview.BaseRecyclerAdapter;
import com.garfield.weishu.base.recyclerview.BaseRecyclerViewHolder;
import com.garfield.weishu.developer.JavaTest;
import com.garfield.weishu.developer.music.ui.MusicControlFragment;
import com.garfield.weishu.developer.test.DeveloperTestFragment;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/11/23.
 */

public class DeveloperFragment extends AppBaseFragment implements BaseRecyclerAdapter.ItemEventListener<String> {

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
        mData.add("2、排序算法");
        mData.add("3、二叉树");
        mData.add("4、多线程");
        mData.add("5、SurfaceView");
        mData.add("6、图像模糊");
        mData.add("7、LayoutManager");
        mData.add("8、Plugin");

        //mData.add("15、音乐控制");
        //mData.add("16、fitsSystemWindows");
        if (BuildConfig.DEBUG) {
            mData.add("100、Test Java");
            mData.add("101、UI");
        }

        DevelopAdapter adapter = new DevelopAdapter(getContext(), mData);
        adapter.setItemEventListener(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AppCache.getContext()));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(String item, int position) {
        String numStr = item.substring(0, item.indexOf('、'));
        int num = Integer.parseInt(numStr);
        switch (num) {
            case 1:
                EventDispatcher.startFragmentEvent(new DeveloperSpeedFragment());
                break;
            case 2:
                EventDispatcher.startFragmentEvent(new DeveloperSortFragment());
                break;
            case 3:
                EventDispatcher.startFragmentEvent(new DeveloperTreeTraverseFragment());
                break;
            case 4:
                EventDispatcher.startFragmentEvent(new DeveloperThreadFragment());
                break;
            case 5:
                EventDispatcher.startFragmentEvent(new DeveloperSurfaceViewFragment());
                break;
            case 6:
                EventDispatcher.startFragmentEvent(new DeveloperBlurFragment());
                break;
            case 8:
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
            case 101:
                EventDispatcher.startFragmentEvent(new DeveloperTestFragment());
                break;
        }
    }

    @Override
    public void onItemLongPressed(String item, int position) {

    }

    private class DevelopAdapter extends BaseRecyclerAdapter<String> {

        private DevelopAdapter(Context context, List<String> items) {
            super(context, items);
        }

        @Override
        public Class getViewHolderClassAtPosition(int position) {
            return RecyclerViewHolder.class;
        }
    }

    public static class RecyclerViewHolder extends BaseRecyclerViewHolder<String> {

        private TextView mTextView;

        @Override
        protected int getLayoutResId() {
            return R.layout.item_string;
        }

        @Override
        protected void inflateView() {
            mTextView = findView(R.id.item_string_text);
            mTextView.setPadding(ScreenUtil.dp2px(20), 0, 0, 0);
        }

        @Override
        protected void refresh(String s) {
            mTextView.setText(s);
        }
    }
}
