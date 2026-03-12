package com.android.system.protection.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    private static final String PREF_NAME = "system_protection_prefs";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_FCM_TOKEN = "fcm_token";

    public static String getDeviceId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String id = prefs.getString(KEY_DEVICE_ID, null);
        if (id == null) {
            id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            saveDeviceId(context, id);
        }
        return id;
    }

    public static void saveDeviceId(Context context, String deviceId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_DEVICE_ID, deviceId).apply();
    }

    public static String getFcmToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_FCM_TOKEN, null);
    }

    public static void saveFcmToken(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_FCM_TOKEN, token).apply();
    }
}