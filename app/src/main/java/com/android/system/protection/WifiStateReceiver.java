package com.android.system.protection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.android.system.protection.fcm.RegistrationService;

public class WifiStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action) ||
                WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
            startForegroundService(context);
            RegistrationService.ensureTokenRegistered(context);
        }
    }

    private void startForegroundService(Context context) {
        Intent serviceIntent = new Intent(context, ForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }
}