package com.garfield.weishu.discovery.news.view;

import android.os.Bundle;
import android.view.View;

import com.garfield.weishu.R;
import com.garfield.weishu.discovery.news.api.ZhihuApi;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

/**
 * Created by gaowei3 on 2016/12/6.
 */

public class ZhihuFragment extends AppBaseFragment {

    {
        setAnimationEnable(false);
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_zhihu;
    }

    @Override
    protected int onGetToolbarTitleResource() {
        return R.string.zhihu_daily;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        loadRootFragment(R.id.fragment_zhihu_content, ZhihuListFragment.newInstance(ZhihuApi.NEWS_TYPE_ZHIHU));
    }


}
