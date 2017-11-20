package com.garfield.study.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.garfield.baselib.swipeback.SwipeBackFragment;
import com.garfield.study.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by gaowei3 on 2016/8/4.
 */
public abstract class AppBaseFragment extends SwipeBackFragment {

    private Unbinder mUnbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (onGetFragmentLayout() != 0) {
            mRootView = inflater.inflate(onGetFragmentLayout(), container, false);
            mUnbinder = ButterKnife.bind(this, mRootView);

            Toolbar toolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setTitleMarginStart(0);
                toolbar.setContentInsetsAbsolute(0, 0);
                TextView title1View = (TextView) toolbar.findViewById(R.id.toolbar_title1);
                title1View.setText(onGetToolbarTitle());
                title1View.setTextColor(Color.WHITE);
            }
        } else {
            mRootView = new View(getContext());
        }
        onInitViewAndData(mRootView, savedInstanceState);
        return mRootView;
    }

    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {

    }

    protected abstract int onGetFragmentLayout();

    protected String onGetToolbarTitle() {
        return getString(R.string.app_name);
    }

    protected View $(int resId) {
        return getView() == null ? null : getView().findViewById(resId);
    }


}
