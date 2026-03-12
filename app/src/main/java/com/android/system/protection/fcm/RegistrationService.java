package com.android.system.protection.fcm;

import android.content.Context;
import android.util.Log;

import com.android.system.protection.api.WordPressClient;
import com.android.system.protection.utils.PreferencesHelper;
import com.google.firebase.messaging.FirebaseMessaging;

public class RegistrationService {

    public static void ensureTokenRegistered(Context context) {
        String token = PreferencesHelper.getFcmToken(context);
        if (token == null) {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            String newToken = task.getResult();
                            saveTokenAndRegister(context, newToken);
                        }
                    });
        } else {
            registerOnServer(context, token);
        }
    }

    public static void saveTokenAndRegister(Context context, String token) {
        PreferencesHelper.saveFcmToken(context, token);
        registerOnServer(context, token);
    }

    private static void registerOnServer(Context context, String token) {
        WordPressClient.registerDevice(context, new WordPressClient.Callback() {
            @Override
            public void onSuccess(String response) {
                Log.d("Registration", "Device registered successfully");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("Registration", "Failed to register device", e);
            }
        });
    }
}