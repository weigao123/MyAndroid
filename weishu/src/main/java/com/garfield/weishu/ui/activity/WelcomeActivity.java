package com.garfield.weishu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.garfield.baselib.BuildConfig;
import com.garfield.weishu.app.SettingsPreferences;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;

/**
 * Created by gaowei3 on 2016/9/6.
 */
public class WelcomeActivity extends AppBaseActivity {

    public static boolean firstEnter = true; // 是否首次进入

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        onIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!BuildConfig.BASE_DEBUG && firstEnter && !isNotify()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    onIntent();
                }
            };
            new Handler().postDelayed(runnable, 1000);
        } else {
            onIntent();
        }
        firstEnter = false;
    }

    private boolean isNotify() {
        return getIntent() != null && getIntent().hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
    }

    private void onIntent() {
        if (!canAutoLogin()) {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        } else {
            Intent intent = getIntent();
            if (intent != null) {
                if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
                    ArrayList<IMMessage> messages = (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
                    if (messages != null && messages.size() > 0) {
                        showMainActivity(new Intent().putExtra(NimIntent.EXTRA_NOTIFY_CONTENT, messages.get(0)));
                        return;
                    }
                }
            }
            showMainActivity(null);
        }
    }

    /**
     * 已经登陆过，自动登陆
     */
    private boolean canAutoLogin() {
        String account = SettingsPreferences.getUserAccount();
        String token = SettingsPreferences.getUserToken();
        return !TextUtils.isEmpty(account) && !TextUtils.isEmpty(token);
    }

    private void showMainActivity(Intent intent) {
        MainActivity.start(WelcomeActivity.this, intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
