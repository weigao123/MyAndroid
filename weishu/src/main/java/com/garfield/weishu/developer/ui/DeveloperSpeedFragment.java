package com.garfield.weishu.developer.ui;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.garfield.baselib.utils.system.SystemUtil;
import com.garfield.weishu.R;
import com.garfield.weishu.developer.view.LikeFrameLayout;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/11/30.
 */

public class DeveloperSpeedFragment extends AppBaseFragment {

    @BindView(R.id.fragment_developer_like_layout)
    LikeFrameLayout mLikeFrameLayout;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_developer_speed;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        SystemUtil.setStatusBarColorK(mActivity, getResources().getColor(R.color.black));

        mLikeFrameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int mTouchX = (int) event.getX();
                    int mTouchY = (int) event.getY();
                    mLikeFrameLayout.addHeart(mTouchX, mTouchY);
                }
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SystemUtil.setStatusBarColorK(mActivity, getResources().getColor(R.color.colorPrimaryDark));
    }
}
