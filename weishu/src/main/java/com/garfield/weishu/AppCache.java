package com.garfield.weishu;

import android.content.Context;


public class AppCache {
    public static final String USER_ACCOUNT = "account";

    private static Context context;

    private static String account;

    private static boolean hasAnimation;

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

    public static boolean isHasAnimation() {
        return hasAnimation;
    }

    public static void setHasAnimation(boolean hasAnimation) {
        AppCache.hasAnimation = hasAnimation;
    }
}
