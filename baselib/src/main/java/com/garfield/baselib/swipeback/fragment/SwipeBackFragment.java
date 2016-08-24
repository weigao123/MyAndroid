package com.garfield.baselib.swipeback.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.baselib.fragmentation.SupportFragment;

/**
 * Created by gaowei3 on 2016/8/24.
 */
public class SwipeBackFragment extends SupportFragment {

    public static final int EDGE_LEFT = ViewDragHelper.EDGE_LEFT;
    public static final int EDGE_RIGHT = ViewDragHelper.EDGE_RIGHT;
    public static final int EDGE_BOTTOM = ViewDragHelper.EDGE_BOTTOM;
    public static final int EDGE_ALL = EDGE_LEFT | EDGE_RIGHT | EDGE_BOTTOM;

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
            public void onDragScrolled(float scrollPercent) {

            }
        });
    }

    protected View attachToSwipeBack(View view) {
        mSwipeBackLayout.attachToFragment(this, view);
        return mSwipeBackLayout;
    }


    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }


    public void setSwipeBackEnable(boolean enable) {
        mSwipeBackLayout.setEnable(enable);
    }

    public void setSwipeBackDirection(int edgeFlags) {
        mSwipeBackLayout.setEdgeOrientation(edgeFlags);
    }
}
