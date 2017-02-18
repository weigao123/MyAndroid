package com.garfield.weishu.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;

/**
 * Created by hzxuwen on 2015/4/13.
 */
public class SettingsPreferences {
    private final static String KEY_STATUS_BAR_NOTIFICATION_CONFIG = "KEY_STATUS_BAR_NOTIFICATION_CONFIG";

    private final static String KEY_NOTIFY_TOGGLE ="key_notify_toggle";
    private final static String KEY_RING_TOGGLE = "KEY_RING_TOGGLE";
    private final static String KEY_VIBRATE_TOGGLE = "KEY_VIBRATE_TOGGLE";
    private final static String KEY_ANIMATION_TOGGLE = "key_animation_toggle";
    private final static String KEY_CROP_TOGGLE = "key_crop_toggle";
    private final static String KEY_ADD_AUTHOR = "key_add_author";


    public static void setNotificationToggle(boolean on) {
        saveBoolean(KEY_NOTIFY_TOGGLE, on);
    }

    public static boolean getNotificationToggle() {
        return getBoolean(KEY_NOTIFY_TOGGLE, true);
    }

    public static void setRingToggle(boolean on) {
        saveBoolean(KEY_RING_TOGGLE, on);
    }

    public static boolean getRingToggle() {
        return getBoolean(KEY_RING_TOGGLE, true);
    }

    public static void setVibrateToggle(boolean on) {
        saveBoolean(KEY_VIBRATE_TOGGLE, on);
    }

    public static boolean getVibrateToggle() {
        return getBoolean(KEY_VIBRATE_TOGGLE, true);
    }

    public static void setAnimation(boolean on) {
        saveBoolean(KEY_ANIMATION_TOGGLE, on);
    }

    public static boolean getAnimation() {
        return getBoolean(KEY_ANIMATION_TOGGLE, true);
    }

    public static void setCropTool(boolean on) {
        saveBoolean(KEY_CROP_TOGGLE, on);
    }

    public static boolean getCropTool() {
        return getBoolean(KEY_CROP_TOGGLE, false);
    }

    public static void setAddAuthor(boolean on) {
        saveBoolean(KEY_ADD_AUTHOR, on);
    }

    public static boolean getAddAuthor() {
        return getBoolean(KEY_ADD_AUTHOR, false);
    }

    private static StatusBarNotificationConfig getConfig() {
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        String jsonString = getSharedPreferences().getString(KEY_STATUS_BAR_NOTIFICATION_CONFIG, "");
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            if (jsonObject == null) {
                return null;
            }
            config.downTimeBegin = jsonObject.getString("downTimeBegin");
            config.downTimeEnd = jsonObject.getString("downTimeEnd");
            config.downTimeToggle = jsonObject.getBoolean("downTimeToggle");
            config.ring = jsonObject.getBoolean("ring");
            config.vibrate = jsonObject.getBoolean("vibrate");
            config.notificationSmallIconId = jsonObject.getIntValue("notificationSmallIconId");
            config.notificationSound = jsonObject.getString("notificationSound");
            config.hideContent = jsonObject.getBoolean("hideContent");
            config.ledARGB = jsonObject.getIntValue("ledargb");
            config.ledOnMs = jsonObject.getIntValue("ledonms");
            config.ledOffMs = jsonObject.getIntValue("ledoffms");
            config.titleOnlyShowAppName = jsonObject.getBoolean("titleOnlyShowAppName");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return config;
    }

    private static void saveStatusBarNotificationConfig(StatusBarNotificationConfig config) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("downTimeBegin", config.downTimeBegin);
            jsonObject.put("downTimeEnd", config.downTimeEnd);
            jsonObject.put("downTimeToggle", config.downTimeToggle);
            jsonObject.put("ring", config.ring);
            jsonObject.put("vibrate", config.vibrate);
            jsonObject.put("notificationSmallIconId", config.notificationSmallIconId);
            jsonObject.put("notificationSound", config.notificationSound);
            jsonObject.put("hideContent", config.hideContent);
            jsonObject.put("ledargb", config.ledARGB);
            jsonObject.put("ledonms", config.ledOnMs);
            jsonObject.put("ledoffms", config.ledOffMs);
            jsonObject.put("titleOnlyShowAppName", config.titleOnlyShowAppName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.putString(KEY_STATUS_BAR_NOTIFICATION_CONFIG, jsonObject.toString());
        editor.commit();
    }

    private static boolean getBoolean(String key, boolean value) {
        return getSharedPreferences().getBoolean(key, value);
    }

    private static void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private static SharedPreferences getSharedPreferences() {
        return AppCache.getContext().getSharedPreferences("weishu." + AppCache.getAccount(), Context.MODE_PRIVATE);
    }
}
