package com.garfield.weishu.developer.ui;

import android.widget.Button;

import com.garfield.weishu.R;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaowei on 17/5/14.
 */

public class DeveloperPluginFragment extends AppBaseFragment {

    @BindView(R.id.developer_plugin)
    Button mButton;

    @Override
    protected String onGetToolbarTitle() {
        return "Plugin";
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_developer_plugin;
    }

    @OnClick(R.id.developer_plugin)
    void plugin() {

    }
}
