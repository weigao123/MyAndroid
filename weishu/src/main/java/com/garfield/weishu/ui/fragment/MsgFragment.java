package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.garfield.baselib.fragmentation.anim.DefaultHorizontalAnimator;
import com.garfield.baselib.fragmentation.anim.FragmentAnimator;
import com.garfield.weishu.R;

/**
 * Created by gaowei3 on 2016/8/4.
 */
public class MsgFragment extends AppBaseFragment {

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_msg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = attachToSwipeBack(mRootView);
        setSwipeBackEnable(true);
        return mRootView;
    }


    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //L.d("MsgFragment  transit: "+transit+"  enter:"+enter+"   nextAnim:"+nextAnim);
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
}
