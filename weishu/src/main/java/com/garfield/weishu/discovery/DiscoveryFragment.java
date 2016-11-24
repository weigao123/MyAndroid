package com.garfield.weishu.discovery;

import android.os.Bundle;
import android.view.View;

import com.garfield.weishu.R;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.helper.browser.BrowserFragment;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

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

    @OnClick(R.id.fragment_discovery_news)
    void openNeteaseNews() {
        EventDispatcher.getFragmentJumpEvent().onShowNews();
    }

    @OnClick(R.id.fragment_discovery_browser)
    void openWebView() {
        EventDispatcher.startFragment(new BrowserFragment());
    }

}
