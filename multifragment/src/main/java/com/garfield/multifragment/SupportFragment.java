package com.garfield.multifragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.garfield.multifragment.anim.DefaultHorizontalAnimator;
import com.garfield.multifragment.anim.FragmentAnimator;

/**
 * Created by gaowei3 on 2016/7/22.
 */
public class SupportFragment extends Fragment {

    protected SupportActivity mSupportActivity;
    private FragmentAnimator mFragmentAnimator;

    private int mContainerId;   // 该Fragment所处的Container的id

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SupportActivity) {
            this.mSupportActivity = (SupportActivity) context;
        } else {
            throw new RuntimeException("Must extends SupportActivity!");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentAnimator = onCreateFragmentAnimator();
        if (mFragmentAnimator == null) {
            mFragmentAnimator = mSupportActivity.getFragmentAnimator();
            if (mFragmentAnimator == null) {
                mFragmentAnimator = new DefaultHorizontalAnimator();
            }
        }
        Bundle bundle = getArguments();
        mContainerId = bundle.getInt(FragmentHelper.FRAGMENT_ARG_CONTAINER);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return AnimationUtils.loadAnimation(mSupportActivity, mFragmentAnimator.getEnter());
        } else {
            return AnimationUtils.loadAnimation(mSupportActivity, mFragmentAnimator.getExit());
        }
    }

    protected FragmentAnimator onCreateFragmentAnimator() {
        return null;
    }

    public int getContainerId() {
        return mContainerId;
    }
}
