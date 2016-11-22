package com.garfield.weishu.discovery;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaowei3 on 2016/11/23.
 */

public class DiscoveryFragment extends AppBaseFragment {

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_discovery;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
    }

    @Override
    protected void onLazyLoad() {
    }

    @OnClick(R.id.fragment_netease_news)
    void openNeteaseNews() {
        EventDispatcher.getFragmentJumpEvent().onShowNews();
    }

}
