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
    protected boolean onEnableSwipe() {
        return true;
    }



    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //L.d("MsgFragment  transit: "+transit+"  enter:"+enter+"   nextAnim:"+nextAnim);
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
}
