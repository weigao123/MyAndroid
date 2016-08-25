
package com.garfield.baselib.swipeback;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.ViewDragHelper;
import android.view.ViewGroup;

import com.garfield.baselib.base.BaseActivity;


public class SwipeBackActivity extends BaseActivity {
    public static final int EDGE_LEFT = ViewDragHelper.EDGE_LEFT;
    public static final int EDGE_RIGHT = ViewDragHelper.EDGE_RIGHT;
    public static final int EDGE_ALL = EDGE_LEFT | EDGE_RIGHT;

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeBackLayout = new SwipeBackLayout(this);
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

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mSwipeBackLayout.attachToActivity(this);
//    }

//    @Override
//    public View findViewById(int id) {
//        View v = super.findViewById(id);
//        if (v == null && mSwipeBackLayout != null)
//            return mSwipeBackLayout.findViewById(id);
//        return v;
//    }

    /**
     * 必须被Activity在onPostCreate里调用，没有调用的话是没有被注入到View里的
     */
    protected void attachToSwipeBack() {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getDecorView().setBackgroundDrawable(null);
        mSwipeBackLayout.attachToActivity(this);
    }

    /**
     * 总开关，需要被Activity打开
     */
    public void setSwipeBackEnable(boolean enable) {
        mSwipeBackLayout.setEnable(enable);
    }

    public void setSwipeBackDirection(int edgeFlags) {
        mSwipeBackLayout.setEdgeOrientation(edgeFlags);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }
}
