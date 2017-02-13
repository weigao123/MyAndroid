package com.garfield.weishu.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.garfield.weishu.R;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/11/30.
 */

public class AboutFragment extends AppBaseFragment {

    @BindView(R.id.fragment_about_version)
    TextView mVersionText;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_about;
    }

    @Override
    protected String onGetToolbarTitleResource() {
        return getString(R.string.about_weishu);
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);

        String version;
        try {
            PackageInfo pi = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_META_DATA);
            version = pi.versionName + "." +pi.versionCode;
        } catch (Exception e) {
            version = "";
        }
        mVersionText.setText(getString(R.string.weishu_version, version));
    }
}
