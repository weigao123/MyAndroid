package com.garfield.weishu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.garfield.baselib.fragmentation.FragmentHelper;
import com.garfield.baselib.fragmentation.SupportFragment;
import com.garfield.baselib.ui.dialog.DialogMaker;
import com.garfield.baselib.utils.L;
import com.garfield.weishu.R;
import com.garfield.weishu.base.event.StartBrotherEvent;
import com.garfield.weishu.nim.NimConfig;
import com.garfield.weishu.nim.cache.LoginSyncHelper;
import com.garfield.weishu.session.session.SessionFragment;
import com.garfield.weishu.ui.fragment.MainFragment;
import com.netease.nimlib.sdk.Observer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.garfield.weishu.AppCache.USER_ACCOUNT;

/**
 * Created by gaowei3 on 2016/7/31.
 */
public class MainActivity extends AppBaseActivity {

    private static final boolean isToBack = true;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_);
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
            // 等待同步数据完成
            boolean syncCompleted = LoginSyncHelper.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
                @Override
                public void onEvent(Void v) {
                    DialogMaker.dismissProgressDialog();
                }
            });
            if (!syncCompleted) {
                DialogMaker.showProgressDialog(MainActivity.this, getString(R.string.prepare_data)).setCanceledOnTouchOutside(false);
            }
        }
        setIsToBack(isToBack);
    }

    @Subscribe
    public void onEvent(StartBrotherEvent event) {
        if (event.targetFragment.getClass() == SessionFragment.class) {
            String account = event.targetFragment.getArguments().getString(USER_ACCOUNT);
            NimConfig.nofityWithTopWithout(account);
        } else {
            NimConfig.nofityWithTopBar();
        }
        startFragment(event.targetFragment);
    }

    @Override
    protected void onSwitchToFragment(Fragment fragment) {
        super.onSwitchToFragment(fragment);
        updateNotification(fragment);
    }

    private void updateNotification(Fragment targetFragment) {
        if (targetFragment == null) {
            NimConfig.nofityWithTopBar();
        } else if (targetFragment.getClass() == MainFragment.class) {
            int position = ((MainFragment)targetFragment).getTabPosition();
            if (position == 0) {
                NimConfig.nofityWithNoTopBar();
            } else {
                NimConfig.nofityWithTopBar();
            }
        } else if (targetFragment.getClass() == SessionFragment.class) {
            String account = targetFragment.getArguments().getString(USER_ACCOUNT);
            NimConfig.nofityWithTopWithout(account);
        } else {
            NimConfig.nofityWithTopBar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment topFragment = getTopFragment();
        if (topFragment.getClass() == MainFragment.class) {
            ((MainFragment) topFragment).switchToFirst();
        }
        updateNotification(topFragment);

//        MainFragment mainFragment = findFragment(MainFragment.class);
//        if (mainFragment != null) {
//            if (mCurrentFragment != null && mCurrentFragment != mainFragment) {
//                popToFragment(MainFragment.class, false);
//            }
//            mainFragment.switchToFirst();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        NimConfig.nofityWithTopBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
