package com.garfield.weishu.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.garfield.baselib.swipeback.SwipeBackActivity;
import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.baselib.utils.system.permission.MPermission;
import com.garfield.baselib.utils.system.permission.annotation.OnMPermissionDenied;
import com.garfield.baselib.utils.system.permission.annotation.OnMPermissionGranted;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaowei3 on 2016/8/25.
 */

/**
 * 要使用ButterKnife，就要重写一些方法
 */
public class AppBaseActivity extends SwipeBackActivity {

    private final int BASIC_PERMISSION_REQUEST_CODE = 110;

    @Nullable @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Nullable @BindView(R.id.toolbar_control_view)
    LinearLayout mToolbarControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (onGetActivityLayout() != 0) {
            setContentView(onGetActivityLayout());
            ButterKnife.bind(this);
            if (mToolbar != null) {
                mToolbar.setTitle(R.string.app_name);
                mToolbar.setTitleTextAppearance(this, R.style.toolbar_text);
            }
            onInitViewAndData(savedInstanceState);
        }
    }

    protected int onGetActivityLayout() {
        return 0;
    }

    protected void onInitViewAndData(Bundle savedInstanceState) {

    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }


    public void requestBasicPermission() {
        MPermission.with(this)
                .addRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                )
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess(){
        L.show("授权成功");
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed(){
        L.show("授权失败");
        finish();
    }

}
