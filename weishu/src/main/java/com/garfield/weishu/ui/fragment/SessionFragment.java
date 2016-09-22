package com.garfield.weishu.ui.fragment;

import android.view.animation.Animation;

import com.garfield.weishu.R;

/**
 * Created by gaowei3 on 2016/8/4.
 */
public class SessionFragment extends AppBaseFragment {

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_session;
    }

    @Override
    protected boolean onEnableSwipe() {
        return true;
    }



    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //L.d("SessionFragment  transit: "+transit+"  enter:"+enter+"   nextAnim:"+nextAnim);
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
}
