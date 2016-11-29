package com.garfield.weishu.setting;

import com.garfield.weishu.R;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

/**
 * Created by gaowei3 on 2016/11/30.
 */

public class AboutFragment extends AppBaseFragment {

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_about;
    }

    @Override
    protected int onGetToolbarTitleResource() {
        return R.string.about_weishu;
    }
}
