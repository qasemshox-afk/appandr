package com.android.system.protection.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

public class PreferencesHelper {

    private static final String PREF_NAME = "system_protection_prefs";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_FCM_TOKEN = "fcm_token";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getDeviceId(Context context) {
        String id = getPrefs(context).getString(KEY_DEVICE_ID, null);
        if (id == null) {
            // استخدام Secure.ANDROID_ID كمعرف فريد للجهاز
            id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            // في حال فشل الحصول عليه (نادر جداً)، يتم توليد معرف عشوائي
            if (id == null || id.isEmpty()) {
                id = java.util.UUID.randomUUID().toString();
            }
            saveDeviceId(context, id);
        }
        return id;
    }

    public static void saveDeviceId(Context context, String deviceId) {
        getPrefs(context).edit().putString(KEY_DEVICE_ID, deviceId).apply();
    }

    public static String getFcmToken(Context context) {
        return getPrefs(context).getString(KEY_FCM_TOKEN, null);
    }

    public static void saveFcmToken(Context context, String token) {
        getPrefs(context).edit().putString(KEY_FCM_TOKEN, token).apply();
    }
}
