package com.garfield.weishu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.garfield.baselib.fragmentation.SupportFragment;
import com.garfield.baselib.ui.dialog.DialogMaker;
import com.garfield.weishu.R;
import com.garfield.weishu.event.StartBrotherEvent;
import com.garfield.weishu.nim.LoginSyncDataStatusObserver;
import com.garfield.weishu.ui.fragment.MainFragment;
import com.netease.nimlib.sdk.Observer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by gaowei3 on 2016/7/31.
 */
public class MainActivity extends AppBaseActivity {

    public static final boolean isBackPressedToBack = true;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    protected int onGetActivityLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitViewAndData(Bundle savedInstanceState) {
        super.onInitViewAndData(savedInstanceState);
        EventBus.getDefault().register(this);
        // 旋转时会非空
        if (savedInstanceState == null) {
            loadRootFragment(R.id.main_activity_fragment_container, (SupportFragment) Fragment.instantiate(this, MainFragment.class.getName()));
        }
        // 等待同步数据完成
        boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
            @Override
            public void onEvent(Void v) {
                DialogMaker.dismissProgressDialog();
            }
        });
        if (!syncCompleted) {
            DialogMaker.showProgressDialog(MainActivity.this, getString(R.string.prepare_data)).setCanceledOnTouchOutside(false);
        }
    }

    @Subscribe
    public void onEvent(StartBrotherEvent event) {
        //setAnimatorEnable(event.targetFragment.getClass() == MsgFragment.class);
        startFragment(event.targetFragment);
    }

    @Override
    public void onBackPressed() {
        procBackPressed(isBackPressedToBack);
    }
}
