package com.android.system.protection.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    public static final String CHANNEL_ID = "system_security_channel";
    public static final int NOTIFICATION_ID = 1;

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // استخدام IMPORTANCE_LOW لضمان استقرار الخدمة على جميع الأجهزة
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "System Security Services",
                    NotificationManager.IMPORTANCE_LOW
            );
            
            channel.setShowBadge(false);
            channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static Notification getForegroundNotification(Context context) {
        // بناء التنبيه بطريقة تدعم جميع الإصدارات
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("نظام الحماية نشط")
                .setContentText("يتم فحص الجهاز وتأمينه في الوقت الفعلي")
                .setSmallIcon(android.R.drawable.ic_shield) // أيقونة درع لتبدو رسمية
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setOngoing(true); // يمنع المستخدم من إزالة التنبيه بالسحب

        // في الإصدارات القديمة جداً، نضمن عدم وجود صوت أو اهتزاز مزعج
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setSound(null).setVibrate(null);
        }

        return builder.build();
    }
}
