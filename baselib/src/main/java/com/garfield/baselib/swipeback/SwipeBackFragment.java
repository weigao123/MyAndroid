package com.garfield.baselib.swipeback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.baselib.base.BaseFragment;
import com.garfield.baselib.utils.L;

/**
 * Created by gaowei3 on 2016/8/24.
 */
public class SwipeBackFragment extends BaseFragment {

    public static final int EDGE_LEFT = ViewDragHelper.EDGE_LEFT;
    public static final int EDGE_RIGHT = ViewDragHelper.EDGE_RIGHT;
    public static final int EDGE_ALL = EDGE_LEFT | EDGE_RIGHT;

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeBackLayout = new SwipeBackLayout(getActivity());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSwipeBackLayout.setLayoutParams(params);
        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state) {

            }

            @Override
            public void onEdgeTouch(int edgeFlag) {

            }

            @Override
            public void onDragScrollChanged(float scrollPercent) {

            }
        });
    }

    /**
     * 必须被Fragment在onCreateView里调用，没有调用的话是没有被注入到View里的
     */
    protected View attachToSwipeBack(View view) {
        mSwipeBackLayout.attachToFragment(this, view);
        return mSwipeBackLayout;
    }

    /**
     * 总开关，需要被Fragment打开
     */
    protected void setSwipeBackEnable(boolean enable) {
        mSwipeBackLayout.setEnable(enable);
    }

    /**
     * 手动滑动到关闭时，返回true
     */
    protected boolean isFragmentSwipeBack() {
        Bundle bundle = getArguments();
        boolean b = bundle != null && bundle.getBoolean(SwipeBackLayout.IS_FRAGMENT_SWIPEBACK);
        return b;
    }

    public void setSwipeBackDirection(int edgeFlags) {
        mSwipeBackLayout.setEdgeOrientation(edgeFlags);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }
}
