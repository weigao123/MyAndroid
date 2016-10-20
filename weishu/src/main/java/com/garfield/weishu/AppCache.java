package com.garfield.weishu;

import android.content.Context;

import com.garfield.weishu.ui.view.ImageLoaderKit;


public class AppCache {
    public static final String USER_ACCOUNT = "account";

    private static Context context;

    private static String account;

    public static void clear() {
        account = null;
    }

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        AppCache.account = account;

    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        AppCache.context = context.getApplicationContext();
    }


}
