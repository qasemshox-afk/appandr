package com.android.system.protection;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.system.protection.utils.NotificationHelper;

public class ForegroundService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationHelper.createNotificationChannel(this);
        startForeground(NotificationHelper.NOTIFICATION_ID, 
                NotificationHelper.getForegroundNotification(this));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // يضمن عودة الخدمة للعمل إذا تم إيقافها
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
