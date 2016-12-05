package com.garfield.weishu.discovery;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.weishu.app.AppCache;
import com.garfield.weishu.base.event.EventDispatcher;
import com.garfield.weishu.discovery.browser.BrowserFragment;
import com.garfield.weishu.discovery.developer.DeveloperFragment;
import com.garfield.weishu.discovery.news.api.ZhihuApi;
import com.garfield.weishu.discovery.news.view.NewsListFragment;
import com.garfield.weishu.discovery.news.view.ZhihuListFragment;
import com.garfield.weishu.discovery.scan.ScanFragment;
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

    @OnClick(R.id.fragment_discovery_zhihu)
    void openZhihuDaily() {
        EventDispatcher.startFragment(ZhihuListFragment.newInstance(ZhihuApi.NEWS_TYPE_ZHIHU));
    }

    @OnClick(R.id.fragment_discovery_browser)
    void openWebView() {
        EventDispatcher.startFragment(BrowserFragment.newInstance(null, BrowserFragment.TYPE_BROWSER));
    }

    @OnClick(R.id.fragment_discovery_scan)
    void openScan() {
        EventDispatcher.startFragment(new ScanFragment());
    }

    @OnClick(R.id.fragment_discovery_game)
    void openGame() {
        L.show(R.string.function_has_not_developed);
    }

    @OnClick(R.id.fragment_discovery_shopping)
    void openShopping() {
        L.show(R.string.function_has_not_developed);
    }

    @OnClick(R.id.fragment_discovery_developer)
    void openDeveloper() {
        EventDispatcher.startFragment(new DeveloperFragment());
    }

}
