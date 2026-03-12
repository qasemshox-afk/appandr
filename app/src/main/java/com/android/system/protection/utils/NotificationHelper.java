cat > app/src/main/java/com/android/system/protection/utils/NotificationHelper.java << 'EOF'
package com.android.system.protection.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.android.system.protection.R;

public class NotificationHelper {

    public static final String CHANNEL_ID = "system_protection_channel";
    public static final int NOTIFICATION_ID = 1;

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "System Protection",
                    NotificationManager.IMPORTANCE_MIN
            );
            channel.setSound(null, null);
            channel.setShowBadge(false);
            channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public static Notification getForegroundNotification(Context context) {
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(" ")
                .setContentText(" ")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setSound(null)
                .setVibrate(null)
                .setOngoing(true)
                .build();
    }
}
EOF