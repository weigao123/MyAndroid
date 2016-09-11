package com.garfield.weishu.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.garfield.weishu.R;
import com.garfield.weishu.config.UserPreferences;

/**
 * Created by gaowei3 on 2016/9/6.
 */
public class WelcomeActivity extends AppBaseActivity {

    private static boolean firstEnter = true; // 是否首次进入

    @Override
    protected int onGetActivityLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //ViewGroup rootView = (ViewGroup) ((ViewGroup) getWindow().getDecorView()).getChildAt(0);
            //rootView.setFitsSystemWindows(true);
            //rootView.setClipToPadding(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (canAutoLogin()) {
                    MainActivity.start(WelcomeActivity.this);
                } else {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }
                finish();
            }
        };
        if (true) {
            new Handler().postDelayed(runnable, 1000);
        } else {
            runnable.run();
        }
    }

    /**
     * 已经登陆过，自动登陆
     */
    private boolean canAutoLogin() {
        String account = UserPreferences.getUserAccount();
        String token = UserPreferences.getUserToken();
        return !TextUtils.isEmpty(account) && !TextUtils.isEmpty(token);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
