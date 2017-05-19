package com.garfield.weishu.app;

import com.garfield.baselib.Cache;


public class AppCache extends Cache {
    public static final String USER_ACCOUNT = "account";

    private static String account;

    private static boolean hasAnimation;

    private static boolean closeToBg;

    public static void clear() {
        account = null;
    }

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        AppCache.account = account;
    }

    public static boolean isHasAnimation() {
        return hasAnimation;
    }

    public static void setHasAnimation(boolean hasAnimation) {
        AppCache.hasAnimation = hasAnimation;
    }

    public static boolean isCloseToBg() {
        return closeToBg;
    }

    public static void setCloseToBg(boolean closeToBg) {
        AppCache.closeToBg = closeToBg;
    }

}
